package xquare.app.xquareinfra.application.github.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.github.dto.response.TokenExchangeResponse
import xquare.app.xquareinfra.application.github.port.`in`.GithubOAuthUseCase
import xquare.app.xquareinfra.application.github.port.out.GithubOAuthPort

@Service
class GithubOAuthService(
    private val githubOAuthPort: GithubOAuthPort
) : GithubOAuthUseCase {
    override fun exchangeToken(code: String): TokenExchangeResponse {
        return githubOAuthPort.exchangeToken(code)
    }
}