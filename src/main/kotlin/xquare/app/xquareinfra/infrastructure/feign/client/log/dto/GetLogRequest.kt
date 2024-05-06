package xquare.app.xquareinfra.infrastructure.feign.client.log.dto

data class GetLogRequest(
    val queries: List<QueryDto>,
    val from: String,
    val to: String
)