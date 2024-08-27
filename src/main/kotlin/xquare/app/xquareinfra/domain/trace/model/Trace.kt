package xquare.app.xquareinfra.domain.trace.model

data class Trace(
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?
) {
    companion object {
        fun createTraceFromSpans(spans: List<Span>, serviceName: String?): Trace {
            require(spans.isNotEmpty()) { "Spans list cannot be empty" }
            val traceId = spans.first().traceId
            require(spans.all { it.traceId == traceId }) { "All spans must have the same traceId" }
            return Trace(traceId, spans, serviceName)
        }
    }
}