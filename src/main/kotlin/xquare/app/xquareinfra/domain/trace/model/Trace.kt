package xquare.app.xquareinfra.domain.trace.model

data class Trace(
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?,
    val dateNano: Long,
    val durationNano: Long
) {
<<<<<<< HEAD
    companion object {
        fun createTraceFromSpans(spans: List<Span>, serviceName: String?): Trace {
            require(spans.isNotEmpty()) { "Spans list cannot be empty" }
            val traceId = spans.first().traceId

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
=======
    fun getRootSpan(): Span? {
        return spans.find { it.parentSpanId == null }
>>>>>>> trace
    }

    fun findRootSpan(): Span? {
        return spans.find { it.parentSpanId == null }
    }
}