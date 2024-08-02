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
        :rotating_light: **에러가 발생하였습니다.** :rotating_light:
        
        **Timestamp:** ${unixToKoreanTime(span.startTimeUnixNano)}
        **Operation:** ${span.name}
        **Trace ID:** ${span.traceId}
        **Span ID:** ${span.spanId}
        
        **Error Message:**
        ```
        ${span.eventsList}
        ```
        
        **추가 정보:**
        - Duration: ${calculateDurationMs(span.startTimeUnixNano, span.endTimeUnixNano)}ms
        - Parent Span ID: ${span.parentSpanId ?: "N/A"}
        
        스퀘어 인프라 웹사이트에서 자세히 확인하고 에러를 고쳐주세요.
        CC: @DevOps @SRE @Backend
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
}