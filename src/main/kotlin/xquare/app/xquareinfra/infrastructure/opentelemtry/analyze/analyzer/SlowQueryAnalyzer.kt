package xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.analyzer

import io.opentelemetry.proto.trace.v1.Span
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.SpanAnalyzer

@Component
class SlowQueryAnalyzer : SpanAnalyzer {
    override fun analyze(span: Span): AnalysisResult? {
        val duration = span.endTimeUnixNano - span.startTimeUnixNano
        val isSlowQuery = duration > 1_000_000_000L &&
                (span.getAttributeValue("db.system")?.hasStringValue() == true ||
                        span.name.contains("SQL", ignoreCase = true))

        return if (isSlowQuery) {
            AnalysisResult(
                level = AnalysisResult.Level.WARNING,
                message = "SLOW QUERY detected in span: ${span.name}, duration: ${duration / 1_000_000L} ms"
            )
        } else null
    }
}