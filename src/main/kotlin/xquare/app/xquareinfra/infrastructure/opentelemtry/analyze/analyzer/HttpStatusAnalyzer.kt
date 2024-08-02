package xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.analyzer

import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.trace.v1.Span
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.SpanAnalyzer

@Component
class HttpStatusAnalyzer : SpanAnalyzer {
    override fun analyze(span: Span): AnalysisResult? {
        val httpStatusValue = span.getAttributeValue("http.status_code")
        val httpStatus = when {
            httpStatusValue?.hasIntValue() == true -> httpStatusValue.intValue
            httpStatusValue?.hasStringValue() == true -> httpStatusValue.stringValue.toIntOrNull()
            else -> null
        }

        return if (httpStatus != null && httpStatus as Int >= 500) {
            AnalysisResult(
                level = AnalysisResult.Level.ERROR,
                message = "HTTP $httpStatus detected in span: ${span.name}"
            )
        } else null
    }
}

fun Span.getAttributeValue(key: String): AnyValue? {
    return attributesList.find { it.key == key }?.value
}