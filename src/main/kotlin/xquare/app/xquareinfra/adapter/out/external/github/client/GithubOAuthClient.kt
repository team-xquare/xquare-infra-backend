package xquare.app.xquareinfra.adapter.out.external.github.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.LoginAccessTokenRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.response.LoginAccessTokenResponse

@FeignClient(
    name = "github-oauth-client",
    url = "https://github.com/"
)
interface GithubOAuthClient {
    @PostMapping("/login/oauth/access_token", consumes = ["application/json"], produces = ["application/json"])
    fun loginAccessToken(
        @RequestBody
        loginAccessTokenRequest: LoginAccessTokenRequest
    ): LoginAccessTokenResponse
}