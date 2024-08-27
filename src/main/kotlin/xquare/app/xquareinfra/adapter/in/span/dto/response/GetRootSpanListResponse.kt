package xquare.app.xquareinfra.adapter.`in`.span.dto.response

data class GetRootSpanListResponse(
    val spans: List<RootSpan>
)

data class RootSpan(
    val date: String,
    val resource: String,
    val durationMs: Long,
    val method: String?,
    val statusCode: Long?
)
