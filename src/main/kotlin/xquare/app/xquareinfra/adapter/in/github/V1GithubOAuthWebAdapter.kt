package xquare.app.xquareinfra.adapter.`in`.github

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.github.dto.request.TokenExchangeRequest
import xquare.app.xquareinfra.adapter.`in`.github.dto.response.TokenExchangeResponse
import xquare.app.xquareinfra.application.github.port.`in`.GithubOAuthUseCase


@RequestMapping("/v1/github")
@RestController
class V1GithubOAuthWebAdapter(
    private val githubOAuthUseCase: GithubOAuthUseCase
) {
    @PostMapping("/exchange-token")
    fun exchangeToken(
        @RequestBody
        request: TokenExchangeRequest
    ): TokenExchangeResponse {
        return githubOAuthUseCase.exchangeToken(request.code)
    }
}