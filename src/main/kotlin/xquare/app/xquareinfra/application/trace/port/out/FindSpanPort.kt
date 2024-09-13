package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Span

interface FindSpanPort {
    fun findRootSpansByServiceName(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Span>
}