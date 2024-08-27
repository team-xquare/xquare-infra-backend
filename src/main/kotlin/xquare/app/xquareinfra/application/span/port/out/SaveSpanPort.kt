package xquare.app.xquareinfra.application.span.port.out

import xquare.app.xquareinfra.domain.span.model.Span

interface SaveSpanPort {
    fun save(span: Span): Span
}