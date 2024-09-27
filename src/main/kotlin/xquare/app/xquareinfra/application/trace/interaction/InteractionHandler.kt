package xquare.app.xquareinfra.domain.trace

import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.Span

interface InteractionHandler {
    fun canHandle(span: Span): Boolean
    fun extractInteractionType(span: Span): InteractionType
}