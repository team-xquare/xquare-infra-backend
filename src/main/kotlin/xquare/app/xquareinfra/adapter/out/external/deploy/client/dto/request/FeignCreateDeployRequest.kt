package xquare.app.xquareinfra.adapter.out.external.deploy.client.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class FeignCreateDeployRequest(
    val email: String,
    val nameKo: String,
    val nameEn: String,
    val team: String,
    val repository: String,
    val organization: String,
    val type: String,
    val useRedis: Boolean,
    val useMySQL: Boolean
)
