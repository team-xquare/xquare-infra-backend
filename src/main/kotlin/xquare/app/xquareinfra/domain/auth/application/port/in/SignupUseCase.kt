package xquare.app.xquareinfra.domain.auth.application.port.`in`

import xquare.app.xquareinfra.domain.auth.adapter.dto.request.SignupRequest

interface SignupUseCase {
    fun signup(signupRequest: SignupRequest)
}