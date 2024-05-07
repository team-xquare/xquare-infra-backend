package xquare.app.xquareinfra.infrastructure.feign.client.log.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class GetLogRequest(
    val queries: List<QueryDto>,
    val from: String,
    val to: String
)