package xquare.app.xquareinfra.application.auth.port.`in`

import xquare.app.xquareinfra.adapter.`in`.auth.dto.request.LoginRequest
import xquare.app.xquareinfra.adapter.`in`.auth.dto.response.TokenResponse

interface LoginUseCase {
    fun login(loginRequest: LoginRequest): TokenResponse
}