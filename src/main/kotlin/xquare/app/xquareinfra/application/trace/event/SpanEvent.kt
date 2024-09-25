package xquare.app.xquareinfra.application.trace.event

import xquare.app.xquareinfra.domain.trace.model.Trace

data class TraceEvent(val source: Any, val trace: Trace)
