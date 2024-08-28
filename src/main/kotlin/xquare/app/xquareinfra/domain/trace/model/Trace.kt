package xquare.app.xquareinfra.domain.trace.model

data class Trace(
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?,
    val dateNano: Long,
    val durationNano: Long
) {
    fun getRootSpan(): Span? {
        return spans.find { it.parentSpanId == null }
    }
}