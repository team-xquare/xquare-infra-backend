package xquare.app.xquareinfra.domain.trace.model

import org.springframework.data.mongodb.core.mapping.Field

data class Span(
    @Field("id")
    val id: String,

    @Field("traceId")
    val traceId: String,

    @Field("spanId")
    val spanId: String,

    @Field("parentSpanId")
    val parentSpanId: String?,

    @Field("name")
    val name: String,

    @Field("kind")
    val kind: Int,

    @Field("startTimeUnixNano")
    val startTimeUnixNano: Long,

    @Field("endTimeUnixNano")
    val endTimeUnixNano: Long,

    @Field("attributes")
    val attributes: Map<String, Any?> = emptyMap(),

    @Field("events")
    val events: List<SpanEvent> = emptyList(),

    @Field("links")
    val links: List<SpanLink> = emptyList(),

    @Field("status")
    val status: SpanStatus,

    @Field("serviceName")
    val serviceName: String?
) {
    private fun getAttributeValue(attribute: String): String? {
        return attributes[attribute]?.toString()
    }

    fun isHttpRequest(): Boolean {
        return getAttributeValue("http_method") != null
    }

    fun getStatusCode(): Int? {
        return getAttributeValue("http_status_code")?.toIntOrNull()
    }

    fun isErrorSpan(): Boolean {
        return getStatusCode()?.let { it >= 500 } ?: false
    }

    fun getServiceNameInScope(): String? {
        return this.serviceName
    }
}
