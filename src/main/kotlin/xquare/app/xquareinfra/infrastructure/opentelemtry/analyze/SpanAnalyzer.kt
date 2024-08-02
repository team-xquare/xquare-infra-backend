package xquare.app.xquareinfra.infrastructure.opentelemtry.analyze

import io.opentelemetry.proto.trace.v1.Span

interface SpanAnalyzer {
    fun analyze(span: Span): AnalysisResult?
}