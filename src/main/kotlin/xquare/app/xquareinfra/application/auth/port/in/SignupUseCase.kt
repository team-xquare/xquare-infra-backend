package xquare.app.xquareinfra.application.auth.port.`in`

import xquare.app.xquareinfra.domain.auth.adapter.dto.request.SignupRequest

interface SignupUseCase {
    fun signup(signupRequest: SignupRequest)
}