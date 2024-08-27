package xquare.app.xquareinfra.domain.trace.model

data class SpanEvent(
    val timeUnixNano: Long,
    val name: String,
    val attributes: Map<String, AttributeValue>
)