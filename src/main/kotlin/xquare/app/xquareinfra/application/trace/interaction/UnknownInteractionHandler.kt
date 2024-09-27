package xquare.app.xquareinfra.application.trace.interaction

import xquare.app.xquareinfra.domain.trace.model.CallStatus
import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

class UnknownInteractionHandler : InteractionHandler {
    override fun canHandle(span: Span): Boolean {
        return true
    }

    override fun extractInteractionType(span: Span): InteractionType {
        return InteractionType.UNKNOWN
    }

    override fun extractStatus(span: Span): CallStatus {
        return CallStatus.UNKNOWN
    }
}