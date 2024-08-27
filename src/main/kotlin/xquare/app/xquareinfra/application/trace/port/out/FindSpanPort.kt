package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Span

interface FindSpanPort {
    fun findRootSpanListByServiceNameInTimeRange(serviceName: String, startTimeUnixNano: Long, endTimeUnixNano: Long): List<Span>
}