package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

data class SpanEventResponse(
    val timeUnixNano: Long,
    val name: String,
    val attributes: Map<String, Any>
)
