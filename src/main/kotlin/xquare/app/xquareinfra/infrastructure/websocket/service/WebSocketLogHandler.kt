import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataClient
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataUtil
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryRequest
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.net.URI
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class WebSocketLogHandler(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort
) : TextWebSocketHandler() {


    private val sessions = Collections.newSetFromMap(ConcurrentHashMap<WebSocketSession, Boolean>())
    private val executor = Executors.newScheduledThreadPool(1)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val uri = session.uri ?: throw IllegalStateException("Session URI cannot be null")
        val queryParams = parseQueryParams(uri)

        val deployName = queryParams["deploy_name"] ?: throw IllegalArgumentException("Deploy name is required")
        val environment = queryParams["environment"] ?: throw IllegalArgumentException("Environment is required")

        sessions.add(session)
        sendInitialLogs(session, deployName, environment)
        schedulePeriodicLogUpdates(session, deployName, environment)
    }

    private fun sendInitialLogs(session: WebSocketSession, deployName: String, environment: String) {
        val response = getContainerLog(deployName, environment, 24 * 60 * 60 * 1000)
        session.sendMessage(TextMessage(response.toString()))
    }

    private fun schedulePeriodicLogUpdates(session: WebSocketSession, deployName: String, environment: String) {
        executor.scheduleAtFixedRate({
            try {
                if (session.isOpen) {
                    val response = getContainerLog(deployName, environment, 15 * 1000)
                    session.sendMessage(TextMessage(response.toString()))
                } else {
                    sessions.remove(session)
                }
            } catch (e: Exception) {
                session.close(CloseStatus.SERVER_ERROR)
            }
        }, 15, 15, TimeUnit.SECONDS)
    }

    private fun getContainerLog(deployName: String, environment: String, durationMillis: Long): GetContainerLogResponse {
        val currentTimeMillis = Instant.now().toEpochMilli()
        val fromMillis = currentTimeMillis - durationMillis

        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, if(environment == "prod") ContainerEnvironment.prod else ContainerEnvironment.stag)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        val request = QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = DataUtil.makeLogQuery(
                        team = deploy.team.teamNameEn,
                        containerName = deployName,
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
        return GetContainerLogResponse(response.results.a.frames[0].data.values[2])
    }

    private fun parseQueryParams(uri: URI): Map<String, String> {
        val queryPairs = mutableMapOf<String, String>()
        val query = uri.rawQuery ?: return emptyMap()
        val pairs = query.split("&")
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            if (idx != -1) {
                queryPairs[pair.substring(0, idx)] = pair.substring(idx + 1)
            }
        }
        return queryPairs
    }
}
