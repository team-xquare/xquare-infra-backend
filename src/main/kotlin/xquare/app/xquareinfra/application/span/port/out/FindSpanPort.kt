package xquare.app.xquareinfra.application.span.port.out

import xquare.app.xquareinfra.domain.span.model.Span

interface FindSpanPort {
    fun findRootSpanListByServiceNameInTimeRange(serviceName: String, startTimeUnixNano: Long, endTimeUnixNano: Long): List<Span>
}