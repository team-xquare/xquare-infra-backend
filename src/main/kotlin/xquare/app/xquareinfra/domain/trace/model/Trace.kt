package xquare.app.xquareinfra.domain.trace.model

data class Trace(
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?,
    var durationNano: Long = 0,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long
) {

    init {
        durationNano = endTimeUnixNano - startTimeUnixNano
    }

    fun getRootSpan(): Span? {
        return spans.find { it.parentSpanId == null }
    }

    fun sortedByAscendingDate(): List<Span> {
        return spans.sortedBy { it.startTimeUnixNano }
    }

    fun getErrorSpan(): Span? {
        return spans.find { span -> span.isErrorSpan() }
    }

    fun isError(): Boolean {
        return spans.any { span ->
            val statusCode = span.getStatusCode()
            statusCode != null && statusCode >= 500
        }
    }
}
