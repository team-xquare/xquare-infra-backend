package xquare.app.xquareinfra.infrastructure.websocket.config

import WebSocketLogHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import xquare.app.xquareinfra.application.log.service.LogService

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val logService: LogService
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(logWebSocketHandler(), "/logs").setAllowedOrigins("*")
    }

    @Bean
    fun logWebSocketHandler(): WebSocketHandler {
        return WebSocketLogHandler(logService)
    }
}
