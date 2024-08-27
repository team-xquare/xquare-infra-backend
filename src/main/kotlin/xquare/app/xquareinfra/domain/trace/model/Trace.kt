package xquare.app.xquareinfra.domain.trace.model

data class Trace(
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?,
    val dateNano: Long,
    val durationNano: Long
) {
    companion object {
        fun createTraceFromSpans(spans: List<Span>, serviceName: String?): Trace {
            require(spans.isNotEmpty()) { "Spans list cannot be empty" }
            val traceId = spans.first().traceId
            require(spans.all { it.traceId == traceId }) { "All spans must have the same traceId" }

            val startTime = spans.minOf { it.startTimeUnixNano }
            val endTime = spans.maxOf { it.endTimeUnixNano }
            val durationMs = endTime - startTime

            return Trace(
                traceId = traceId,
                spans = spans,
                serviceName = serviceName,
                dateNano = startTime,
                durationNano = durationMs
            )
        }
    }

    fun findRootSpan(): Span? {
        return spans.find { it.parentSpanId == null }
    }
}