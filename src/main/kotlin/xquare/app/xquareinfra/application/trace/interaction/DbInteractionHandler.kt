package xquare.app.xquareinfra.application.trace.interaction

import xquare.app.xquareinfra.domain.trace.model.CallStatus
import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

class DbInteractionHandler : InteractionHandler {
    override fun canHandle(span: Span): Boolean {
        return span.attributes.containsKey("db_system")
    }

    override fun extractInteractionType(span: Span): InteractionType {
        return InteractionType.DB
    }

    override fun extractStatus(span: Span): CallStatus {
        // TODO :: 실제 성공여부 판단로직 작성
        return CallStatus.SUCCESS
    }
}