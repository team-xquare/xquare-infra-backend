package xquare.app.xquareinfra.adapter.out.external.github.client.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class LoginAccessTokenResponse(
    val accessToken: String,
    val scope: String,
    val tokenType: String,
)
