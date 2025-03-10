package xquare.app.xquareinfra.adapter.out.external.github.client.dto.response

data class LoginAccessTokenResponse(
    val access_token: String,
    val scope: String,
    val token_type: String,
)
