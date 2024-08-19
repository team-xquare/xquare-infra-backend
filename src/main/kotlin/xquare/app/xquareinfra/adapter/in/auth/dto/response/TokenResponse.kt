package xquare.app.xquareinfra.adapter.`in`.auth.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val email: String
)