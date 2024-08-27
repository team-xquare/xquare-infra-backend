package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Span

interface SaveSpanPort {
    fun save(span: Span): Span
}