package xquare.app.xquareinfra.domain.deploy.adapter.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class ApproveDeployRequest(
    val accessKey: String,
    val secretKey: String
)
