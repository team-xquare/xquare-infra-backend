package xquare.app.xquareinfra.domain.auth.adapter.dto.request

data class SignupRequest(
    val email: String,
    val accountId: String,
    val password: String
)
