package xquare.app.xquareinfra.infrastructure.telemetry.analyze.analyzer

import io.opentelemetry.proto.trace.v1.Span
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.telemetry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.telemetry.analyze.SpanAnalyzer
import xquare.app.xquareinfra.infrastructure.telemetry.analyze.getAttributeValue

@Component
class HttpStatusAnalyzer : SpanAnalyzer {
    override fun analyze(span: Span): AnalysisResult? {
        val httpStatusValue = span.getAttributeValue("http.status_code")
        val httpStatus = when {
            httpStatusValue?.hasIntValue() == true -> httpStatusValue.intValue
            httpStatusValue?.hasStringValue() == true -> httpStatusValue.stringValue.toLongOrNull()
            else -> null
        }

        return if (httpStatus != null && httpStatus >= 500) {
            AnalysisResult(
                level = AnalysisResult.Level.ERROR,
                message = "HTTP $httpStatus detected in span: ${span.name}"
            )
        } else null
    }
}

