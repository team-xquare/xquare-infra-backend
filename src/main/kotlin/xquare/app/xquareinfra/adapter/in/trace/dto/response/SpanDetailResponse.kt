package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

data class SpanDetailResponse(
    val traceId: String,
    val spanId: String,
    val name: String,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
    val attributes: Map<String, Any?> = emptyMap(),
    val events: List<SpanEventResponse> = emptyList(),
)
