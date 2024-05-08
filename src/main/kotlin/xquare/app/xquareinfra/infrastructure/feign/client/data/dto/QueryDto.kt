package xquare.app.xquareinfra.infrastructure.feign.client.data.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class QueryDto(
    val expr: String,
    val refId: String,
    val datasource: String,
    val hide: Boolean,
    val queryType: String,
    val maxLines: Int,
    val intervalMs: Int,
    val maxDataPoints: Int,
    val legendFormat: String,
    val datasourceId: Int
)

