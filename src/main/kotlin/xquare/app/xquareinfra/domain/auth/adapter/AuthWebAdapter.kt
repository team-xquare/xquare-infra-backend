package xquare.app.xquareinfra.domain.auth.adapter

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.auth.adapter.dto.request.LoginRequest
import xquare.app.xquareinfra.domain.auth.application.port.`in`.LoginUseCase

@RequestMapping("/auth")
@RestController
class AuthWebAdapter(
    private val loginUseCase: LoginUseCase
) {
    @PostMapping("/login")
    fun login(
        @RequestBody
        loginRequest: LoginRequest
    ) = loginUseCase.login(loginRequest)
}