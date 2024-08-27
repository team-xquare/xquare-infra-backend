package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

data class GetTraceListResponse(
    val traces: List<Trace>
)

data class Trace(
    val dateNano: Long,
    val resource: String?,
    val durationMs: Long,
    val method: String?,
    val statusCode: Long?
)
