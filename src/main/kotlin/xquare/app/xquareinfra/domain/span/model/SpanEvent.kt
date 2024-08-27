package xquare.app.xquareinfra.domain.span.model

data class SpanEvent(
    val timeUnixNano: Long,
    val name: String,
    val attributes: Map<String, AttributeValue>
)