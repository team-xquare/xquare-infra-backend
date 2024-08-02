package xquare.app.xquareinfra.infrastructure.feign.client.deploy.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class DeployResponse(
    @JsonProperty("nameKo")
    val nameKo: String,
    @JsonProperty("nameEn")
    val nameEn: String,
    @JsonProperty("repository")
    val repository: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("team")
    val team: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("organization")
    val organization: String,
    @JsonProperty("useRedis")
    val useRedis: Boolean,
    @JsonProperty("useMysql")
    val useMysql: Boolean,
    @JsonProperty("isApproved")
    val isApproved: Boolean
)