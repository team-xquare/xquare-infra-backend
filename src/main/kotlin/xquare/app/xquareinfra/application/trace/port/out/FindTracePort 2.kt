package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Trace

interface FindTracePort {
    fun findTracesByServiceNameInTimeRange(serviceName: String, startTimeNano: Long, endTimeNano: Long): List<Trace>
}