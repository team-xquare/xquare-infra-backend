package xquare.app.xquareinfra.application.log.service

import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.adapter.dto.response.LogEntry
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.external.data.client.DataClient
import xquare.app.xquareinfra.infrastructure.external.data.util.DataUtil
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.QueryRequest
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Service
class LogService(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort
) {
    private val executor = Executors.newScheduledThreadPool(1)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun sendInitialLogs(session: WebSocketSession, deployId: UUID, environment: String) {
        val response = getContainerLog(deployId, environment, 3 * 60 * 60 * 1000)
        val logMessages = response.logs.joinToString("\n") { "${(it as LogEntry).timestamp}: ${(it as LogEntry).body}" }
        session.sendMessage(TextMessage(logMessages))
    }

    fun schedulePeriodicLogUpdates(session: WebSocketSession, deployId: UUID, environment: String) {
        executor.scheduleAtFixedRate({
            try {
                if (session.isOpen) {
                    val response = getContainerLog(deployId, environment, 15 * 1000)
                    val logMessages = response.logs.joinToString("\n") { "${(it as LogEntry).timestamp}: ${(it as LogEntry).body}" }
                    session.sendMessage(TextMessage(logMessages))
                } else {
                    session.close(CloseStatus.SERVER_ERROR)
                }
            } catch (e: Exception) {
                session.close(CloseStatus.SERVER_ERROR)
            }
        }, 15, 15, TimeUnit.SECONDS)
    }

    private fun getContainerLog(deployId: UUID, environment: String, durationMillis: Long): GetContainerLogResponse {
        val currentTimeMillis = Instant.now().toEpochMilli()
        val fromMillis = currentTimeMillis - durationMillis

        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, if (environment == "prod") ContainerEnvironment.prod else ContainerEnvironment.stag)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        val request = QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = DataUtil.makeLogQuery(
                        containerEnvironment = if (environment == "prod") ContainerEnvironment.prod else ContainerEnvironment.stag,
                        deploy = deploy
                    ),
                    refId = "A",
                    datasource = "loki",
                    hide = false,
                    queryType = "range",
                    intervalMs = 2000,
                    maxDataPoints = 630,
                    maxLines = 3000,
                    legendFormat = "",
                    datasourceId = 3
                )
            ),
            from = fromMillis.toString(),
            to = currentTimeMillis.toString()
        )

        val response = dataClient.query(request)

        val frames = response.results?.a?.frames

        val logs = frames?.flatMap { frame ->
            frame.data?.let { data ->
                if (data.values.size >= 3) {
                    val timestamps = (data.values[1] as List<Long>).reversed()
                    val bodies = (data.values[2] as List<String>).reversed()
                    timestamps.zip(bodies) { timestamp, body ->
                        val kstTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Seoul"))
                        LogEntry(kstTime.format(formatter), body)
                    }
                } else {
                    emptyList()
                }
            } ?: emptyList()
        }?.sortedBy { it.timestamp } ?: emptyList()

        return GetContainerLogResponse(logs)
    }

}
