package xquare.app.xquareinfra.adapter.`in`.auth.dto.request

data class SignupRequest(
    val email: String,
    val accountId: String,
    val password: String
)
