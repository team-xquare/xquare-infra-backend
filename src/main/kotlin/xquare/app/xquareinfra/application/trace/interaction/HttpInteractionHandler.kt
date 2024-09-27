package xquare.app.xquareinfra.application.trace.interaction

import xquare.app.xquareinfra.domain.trace.model.CallStatus
import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

class HttpInteractionHandler : InteractionHandler {
    override fun canHandle(span: Span): Boolean {
        return span.attributes.containsKey("http_method") && span.attributes.containsKey("net_peer_name")
    }

    override fun extractInteractionType(span: Span): InteractionType {
        return InteractionType.HTTP
    }

    override fun extractStatus(span: Span): CallStatus {
        val statusCode = span.attributes["http_status_code"] as? Int
        return when {
            statusCode == null -> CallStatus.UNKNOWN
            statusCode < 400 -> CallStatus.SUCCESS
            else -> CallStatus.SUCCESS
        }
    }
}