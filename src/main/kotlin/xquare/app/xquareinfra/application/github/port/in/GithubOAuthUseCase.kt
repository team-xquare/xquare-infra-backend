package xquare.app.xquareinfra.application.github.port.`in`

import xquare.app.xquareinfra.adapter.`in`.github.dto.response.TokenExchangeResponse

interface GithubOAuthUseCase {
    fun exchangeToken(code: String): TokenExchangeResponse
}