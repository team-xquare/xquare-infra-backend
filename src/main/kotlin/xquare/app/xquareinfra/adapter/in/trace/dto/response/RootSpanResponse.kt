package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

data class RootSpanResponse(
    val traceId: String,
    val date: String,
    val resource: String,
    val durationMs: Long,
    val method: String?,
    val statusCode: Long?
)
