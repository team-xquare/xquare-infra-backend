package xquare.app.xquareinfra.infrastructure.external.feign.client.dsm.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import xquare.app.xquareinfra.infrastructure.external.client.dsm.dto.DsmUserRole

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GetDsmUserInfoResponse(
    val id: String,
    val accountId: String,
    val password: String,
    val name: String,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val userRole: DsmUserRole,
    val birthDay: String,
    @JsonProperty("profileImgUrl")
    val profileImgUrl: String,
    @JsonProperty("clubName")
    val clubName: String? = null
)
