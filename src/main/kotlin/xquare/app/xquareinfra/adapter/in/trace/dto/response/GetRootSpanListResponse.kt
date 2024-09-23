package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

data class GetRootSpanListResponse(
    val spans: List<RootSpanResponse>,
    val nextCursorBeforeSpanId: String?,
    val nextCursorAfterSpanId: String?,
    val hasMoreBefore: Boolean,
    val hasMoreAfter: Boolean
)
