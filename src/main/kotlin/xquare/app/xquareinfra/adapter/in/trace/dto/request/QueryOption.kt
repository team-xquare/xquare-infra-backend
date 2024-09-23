package xquare.app.xquareinfra.adapter.`in`.trace.dto.request

data class QueryOption(
    val afterSpanId: String? = null,
    val beforeSpanId: String? = null,
    val limit: Long = 50
)
