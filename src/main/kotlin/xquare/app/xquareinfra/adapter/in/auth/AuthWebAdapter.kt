package xquare.app.xquareinfra.adapter.`in`.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.auth.dto.request.LoginRequest
import xquare.app.xquareinfra.adapter.`in`.auth.dto.request.SignupRequest
import xquare.app.xquareinfra.application.auth.port.`in`.LoginUseCase
import xquare.app.xquareinfra.application.auth.port.`in`.SignupUseCase

@RequestMapping("/v1/auth")
@RestController
class AuthWebAdapter(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase
) {
    @PostMapping("/login")
    fun login(
        @RequestBody
        loginRequest: LoginRequest
    ) = loginUseCase.login(loginRequest)

    @PostMapping("/signup")
    fun signup(
        @RequestBody
        signupRequest: SignupRequest
    ) = signupUseCase.signup(signupRequest)
}