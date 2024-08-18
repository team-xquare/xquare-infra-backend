package xquare.app.xquareinfra.application.auth.port.`in`

import xquare.app.xquareinfra.adapter.`in`.auth.dto.request.SignupRequest

interface SignupUseCase {
    fun signup(signupRequest: SignupRequest)
}