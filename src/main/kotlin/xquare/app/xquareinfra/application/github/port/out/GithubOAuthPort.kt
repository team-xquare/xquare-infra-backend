package xquare.app.xquareinfra.application.github.port.out

import xquare.app.xquareinfra.adapter.`in`.github.dto.response.TokenExchangeResponse

interface GithubOAuthPort {
    fun exchangeToken(code: String): TokenExchangeResponse
}