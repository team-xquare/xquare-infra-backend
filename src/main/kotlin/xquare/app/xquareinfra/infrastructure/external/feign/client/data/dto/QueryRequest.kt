package xquare.app.xquareinfra.infrastructure.external.client.data.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class QueryRequest(
    val queries: List<QueryDto>,
    val from: String,
    val to: String
)