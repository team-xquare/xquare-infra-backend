package xquare.app.xquareinfra.infrastructure.feign.client.log.dto

data class QueryDto(
    val expr: String,
    val refId: String,
    val datasource: String
)

