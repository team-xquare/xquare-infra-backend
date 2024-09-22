package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.infrastructure.listener.TraceEventCache

interface SaveTraceEventCachePort {
    fun save(traceEventCache: TraceEventCache)
}