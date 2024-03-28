package xquare.app.xquareinfra.domain.auth.application.port.`in`

import xquare.app.xquareinfra.domain.auth.adapter.dto.request.LoginRequest
import xquare.app.xquareinfra.domain.auth.adapter.dto.response.TokenResponse

interface LoginUseCase {
    fun login(loginRequest: LoginRequest): TokenResponse
}