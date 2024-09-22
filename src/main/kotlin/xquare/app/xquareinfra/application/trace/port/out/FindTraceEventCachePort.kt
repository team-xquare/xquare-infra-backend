package xquare.app.xquareinfra.application.trace.port.out

interface FindTraceEventCachePort {
    fun existsById(traceId: String): Boolean
}