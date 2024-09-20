package xquare.app.xquareinfra.infrastructure.webhook

import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.infrastructure.util.TimeUtil

object AlertMessageGenerator {
    fun makeErrorMessage(span: Span): String {
        val content = """
        :rotating_light: **에러가 발생하였습니다.** :rotating_light:
        
        **Timestamp:** ${TimeUtil.unixToKoreanTime(span.startTimeUnixNano)}
        **Operation:** ${span.name}
        **Trace ID:** ${span.traceId}
        **Span ID:** ${span.spanId}
        
        **Error Message:**
        ```
        ${span.events}
        ```
        
        **추가 정보:**
        - Duration: ${TimeUtil.calculateDurationMs(span.startTimeUnixNano, span.endTimeUnixNano)}ms
        - Parent Span ID: ${span.parentSpanId ?: "N/A"}
        
        스퀘어 인프라 웹사이트에서 자세히 확인하고 에러를 고쳐주세요.
        """.trimIndent()

        return content
    }
}