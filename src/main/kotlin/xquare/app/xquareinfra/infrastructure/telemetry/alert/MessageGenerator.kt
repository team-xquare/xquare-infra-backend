package xquare.app.xquareinfra.infrastructure.telemetry.alert

import io.opentelemetry.proto.trace.v1.Span
import xquare.app.xquareinfra.infrastructure.telemetry.analyze.AnalysisResult
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

object MessageGenerator {
    fun makeErrorMessage(span: Span, result: AnalysisResult): String {
        val content = """
        :rotating_light: **에러 알림** :rotating_light:
        
        >>> ### 기본 정보
        • **에러 정보:** ${result.message}
        • **시간:** ${unixToKoreanTime(span.startTimeUnixNano / 1_000_000)}
        • **작업:** \`${span.name}\`
        • **소요 시간:** ${calculateDurationMs(span.startTimeUnixNano, span.endTimeUnixNano)}ms
        
        ### 추적 정보
        • **Trace ID:** \`${bytesToHex(span.traceId.toByteArray())}\`
        • **Span ID:** \`${bytesToHex(span.spanId.toByteArray())}\`
        • **Parent Span ID:** \`${if (span.parentSpanId.isEmpty) "N/A" else bytesToHex(span.parentSpanId.toByteArray())}\`
        ---
        """.trimIndent()

        return content
    }

    fun unixToKoreanTime(unixTimestamp: Long): String {
        val instant = Instant.ofEpochMilli(unixTimestamp)
        val koreanZoneId = ZoneId.of("Asia/Seoul")
        val koreanTime = instant.atZone(koreanZoneId)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return koreanTime.format(formatter)
    }

    fun calculateDurationMs(startTimeNano: Long, endTimeNano: Long): Long {
        return abs(endTimeNano - startTimeNano) / 1_000_000
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}