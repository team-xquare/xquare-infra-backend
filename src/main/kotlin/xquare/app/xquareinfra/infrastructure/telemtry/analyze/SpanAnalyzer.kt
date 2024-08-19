package xquare.app.xquareinfra.infrastructure.telemtry.analyze

import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.trace.v1.Span

interface SpanAnalyzer {
    fun analyze(span: Span): AnalysisResult?
}

fun Span.getAttributeValue(key: String): AnyValue? {
    return attributesList.find { it.key == key }?.value
}