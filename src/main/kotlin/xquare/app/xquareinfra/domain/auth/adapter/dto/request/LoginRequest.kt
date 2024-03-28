package xquare.app.xquareinfra.domain.auth.adapter.dto.request

data class LoginRequest(
    val accountId: String,
    val password: String
)
