package xquare.app.xquareinfra.application.auth.port.`in`

import xquare.app.xquareinfra.domain.auth.adapter.dto.request.LoginRequest
import xquare.app.xquareinfra.domain.auth.adapter.dto.response.TokenResponse

interface LoginUseCase {
    fun login(loginRequest: LoginRequest): TokenResponse
}