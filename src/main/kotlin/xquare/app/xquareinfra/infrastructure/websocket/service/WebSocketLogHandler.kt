import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import xquare.app.xquareinfra.domain.log.service.LogService
import java.net.URI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class WebSocketLogHandler(
    private val logService: LogService
) : TextWebSocketHandler() {

    private val sessions = Collections.newSetFromMap(ConcurrentHashMap<WebSocketSession, Boolean>())
    private val executor = Executors.newScheduledThreadPool(1)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val uri = session.uri ?: throw IllegalStateException("Session URI cannot be null")
        val queryParams = parseQueryParams(uri)

        val deployId = queryParams["deployId"] ?: throw IllegalArgumentException("Deploy Id is required")
        val environment = queryParams["environment"] ?: throw IllegalArgumentException("Environment is required")

        sessions.add(session)
        logService.sendInitialLogs(session, UUID.fromString(deployId), environment)
        logService.schedulePeriodicLogUpdates(session, UUID.fromString(deployId), environment)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
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
