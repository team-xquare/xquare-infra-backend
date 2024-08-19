package xquare.app.xquareinfra.adapter.`in`.deploy.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class ApproveDeployRequest(
    val accessKey: String,
    val secretKey: String
)
