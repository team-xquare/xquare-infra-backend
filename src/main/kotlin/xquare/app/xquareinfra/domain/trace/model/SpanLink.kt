package xquare.app.xquareinfra.domain.trace.model

data class SpanLink(
    val traceId: String,
    val spanId: String,
    val attributes: Map<String, AttributeValue>
)