package xquare.app.xquareinfra.infrastructure.opentelemtry.alert

import io.opentelemetry.proto.trace.v1.Span
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.AnalysisResult
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

object MessageGenerator {
    fun makeErrorMessage(span: Span, result: AnalysisResult): String {
        val content = """
        :rotating_light: **에러 알림** :rotating_light:
        
        >>> ### 기본 정보
        • **시간:** ${unixToKoreanTime(span.startTimeUnixNano / 1_000_000)}
        • **작업:** \`${span.name}\`
        • **소요 시간:** ${calculateDurationMs(span.startTimeUnixNano, span.endTimeUnixNano)}ms
        
        ### 추적 정보
        • **Trace ID:** \`${bytesToHex(span.traceId.toByteArray())}\`
        • **Span ID:** \`${bytesToHex(span.spanId.toByteArray())}\`
        • **Parent Span ID:** \`${if (span.parentSpanId.isEmpty) "N/A" else bytesToHex(span.parentSpanId.toByteArray())}\`
        
        ### 조치 사항
        1. 스퀘어 인프라 웹사이트에서 자세한 내용을 확인하세요.
        2. 에러를 분석하고 필요한 조치를 취해주세요.
        
        <@&DevOpsRoleID> <@&SRERoleID> <@&BackendRoleID>
        
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