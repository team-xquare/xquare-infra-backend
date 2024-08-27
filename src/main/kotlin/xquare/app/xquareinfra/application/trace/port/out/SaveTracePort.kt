package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.domain.trace.model.Trace

interface SaveTracePort {
    fun save(trace: Trace): Trace
}