package xquare.app.xquareinfra.application.trace.interaction

import xquare.app.xquareinfra.domain.trace.model.CallStatus
import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

interface InteractionHandler {
    fun canHandle(span: Span): Boolean
    fun extractInteractionType(span: Span): InteractionType
    fun extractStatus(span: Span): CallStatus
}