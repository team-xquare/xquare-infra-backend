package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Trace

interface FindTracePort {
<<<<<<< HEAD
    fun findTraceListByServiceNameInTimeRange(serviceName: String, startTimeUnixNano: Long, endTimeUnixNano: Long): List<Trace>
=======
    fun findTracesByServiceNameInTimeRange(serviceName: String, startTimeNano: Long, endTimeNano: Long): List<Trace>
>>>>>>> trace
}