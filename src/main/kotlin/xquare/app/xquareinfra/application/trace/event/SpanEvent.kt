package xquare.app.xquareinfra.application.trace.event

import xquare.app.xquareinfra.domain.trace.model.Span

data class SpanEvent(val source: Any, val span: Span)
