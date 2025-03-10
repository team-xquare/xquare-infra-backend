package xquare.app.xquareinfra.adapter.out.external.github.client.dto.request

data class LoginAccessTokenRequest(
    val client_secret: String,
    val client_id: String,
    val redirect_uri: String,
    val code: String,
)
