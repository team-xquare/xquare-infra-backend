package xquare.app.xquareinfra.domain.span.model

import com.google.protobuf.ByteString
import io.opentelemetry.proto.common.v1.AnyValue

data class Span(
    val id: String,
    val traceId: String,
    val spanId: String,
    val parentSpanId: String?,
    val name: String,
    val kind: Int,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
    val attributes: Map<String, AttributeValue>,
    val events: List<SpanEvent>,
    val links: List<SpanLink>,
    val status: SpanStatus,
    val rootServiceName: String?
) {
    companion object {
        fun createSpanFromOTel(otelSpan: io.opentelemetry.proto.trace.v1.Span, rootServiceName: String?): Span {
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
                    message = otelSpan.status.message
                ),
                rootServiceName = rootServiceName
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
