package xquare.app.xquareinfra.application.trace.interaction

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.trace.model.Span

@Component
class InteractionHandlerFactory {
    private val handlers: List<InteractionHandler> = listOf(
        HttpInteractionHandler(),
        DbInteractionHandler(),
        KafkaInteractionHandler(),
        UnknownInteractionHandler()
    )

    fun getHandler(span: Span): InteractionHandler {
        return handlers.firstOrNull { it.canHandle(span) } ?: UnknownInteractionHandler()
    }
}