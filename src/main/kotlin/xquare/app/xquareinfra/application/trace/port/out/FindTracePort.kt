package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Trace

interface FindTracePort {
    fun findTraceListByServiceNameInTimeRange(serviceName: String, startTimeUnixNano: Long, endTimeUnixNano: Long): List<Trace>
}