package xquare.app.xquareinfra.infrastructure.external.data.client.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.QueryDto

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class QueryRequest(
    val queries: List<QueryDto>,
    val from: String,
    val to: String
)