package xquare.app.xquareinfra.domain.trace.model

import com.google.protobuf.ByteString
import io.opentelemetry.proto.common.v1.AnyValue
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
    val status: SpanStatus
){
    fun getAttributeValue(attribute: String): String? {
        return attributes[attribute]?.toString()
    }

    companion object {
        fun createSpanFromOTel(otelSpan: io.opentelemetry.proto.trace.v1.Span): Span {
            return Span(
                id = "${otelSpan.traceId.toHexString()}${otelSpan.spanId.toHexString()}",
                traceId = otelSpan.traceId.toHexString(),
                spanId = otelSpan.spanId.toHexString(),
                parentSpanId = if (otelSpan.parentSpanId.isEmpty()) null else otelSpan.parentSpanId.toHexString(),
                name = otelSpan.name,
                kind = otelSpan.kind.number,
                startTimeUnixNano = otelSpan.startTimeUnixNano,
                endTimeUnixNano = otelSpan.endTimeUnixNano,
                attributes = otelSpan.attributesList.associate { it.key to it.value.toAttributeValue() },
                events = otelSpan.eventsList.map { event ->
                    SpanEvent(
                        timeUnixNano = event.timeUnixNano,
                        name = event.name,
                        attributes = event.attributesList.associate { it.key to it.value.toAttributeValue() }
                    )
                },
                links = otelSpan.linksList.map { link ->
                    SpanLink(
                        traceId = link.traceId.toHexString(),
                        spanId = link.spanId.toHexString(),
                        attributes = link.attributesList.associate { it.key to it.value.toAttributeValue() }
                    )
                },
                status = SpanStatus(
                    code = otelSpan.status.code.number,
                    description = otelSpan.status.message
                )
            )
        }

        private fun AnyValue.toAttributeValue(): AttributeValue {
            return when {
                hasStringValue() -> AttributeValue(stringValue = stringValue)
                hasIntValue() -> AttributeValue(intValue = intValue)
                hasBoolValue() -> AttributeValue(boolValue = boolValue)
                hasDoubleValue() -> AttributeValue(doubleValue = doubleValue)
                hasArrayValue() -> AttributeValue(arrayValue = arrayValue.valuesList.map { it.toAttributeValue() })
                else -> AttributeValue(stringValue = "")
            }
        }

        fun ByteString.toHexString(): String {
            return this.toByteArray().joinToString("") { "%02x".format(it) }
        }
    }
}
