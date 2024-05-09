package xquare.app.xquareinfra.domain.auth.adapter.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val email: String
)