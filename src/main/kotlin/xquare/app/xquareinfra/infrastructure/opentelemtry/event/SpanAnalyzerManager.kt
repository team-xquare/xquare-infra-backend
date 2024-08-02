package xquare.app.xquareinfra.infrastructure.opentelemtry.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.opentelemtry.alert.OpenTelemetryAlertManager
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.SpanAnalyzer

@Component
class SpanAnalyzerManager(
    private val analyzers: List<SpanAnalyzer>,
    private val openTelemetryAlertManager: OpenTelemetryAlertManager
) {
    @EventListener
    fun onSpanReceived(event: SpanReceivedEvent) {
        val results = analyzers.mapNotNull { it.analyze(event.span) }
        results.forEach { result ->
            when (result.level) {
                AnalysisResult.Level.INFO -> println("INFO: ${result.message}")
                AnalysisResult.Level.WARNING -> println("WARNING: ${result.message}")
                AnalysisResult.Level.ERROR ->
                    openTelemetryAlertManager.notification(
                        analysisResult = result,
                        span = event.span
                    )
            }
        }
    }
}