package xquare.app.xquareinfra.domain.log.service

import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataClient
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataUtil
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryRequest
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class LogService(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort
) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun sendInitialLogs(session: WebSocketSession, deployId: UUID, environment: String) {
        val response = getContainerLog(deployId, environment, 24 * 60 * 60 * 1000)
        session.sendMessage(TextMessage(response.toString()))
    }

    fun schedulePeriodicLogUpdates(session: WebSocketSession, deployId: UUID, environment: String) {
        executor.scheduleAtFixedRate({
            try {
                if (session.isOpen) {
                    val response = getContainerLog(deployId, environment, 15 * 1000)
                    session.sendMessage(TextMessage(response.toString()))
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

        println("fromMillis : ${fromMillis}\n currentTimeMillis : ${currentTimeMillis}\n")

        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, if(environment == "prod") ContainerEnvironment.prod else ContainerEnvironment.stag)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        val request = QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = DataUtil.makeLogQuery(
                        team = deploy.team.teamNameEn,
                        containerName = deploy.deployName,
                        serviceType = deploy.deployType,
                        envType = if(environment == "prod") ContainerEnvironment.prod else ContainerEnvironment.stag
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

        val frames = response.results.a.frames
        return GetContainerLogResponse(frames.last().data.values[2])
    }
}
