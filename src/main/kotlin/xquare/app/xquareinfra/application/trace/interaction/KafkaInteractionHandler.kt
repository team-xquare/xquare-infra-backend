package xquare.app.xquareinfra.application.trace.interaction

import xquare.app.xquareinfra.domain.trace.model.CallStatus
import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

class KafkaInteractionHandler : InteractionHandler {
    override fun canHandle(span: Span): Boolean {
        return span.attributes.containsKey("messaging_system") &&
                span.attributes["messaging_system"] == "kafka"
    }

    override fun extractInteractionType(span: Span): InteractionType {
        return InteractionType.KAFKA
    }

    override fun extractStatus(span: Span): CallStatus {
        // TODO :: 실제 성공여부 판단로직 작성
        return CallStatus.SUCCESS
    }
}