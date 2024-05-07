package xquare.app.xquareinfra.domain.user.adapter

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.user.adapter.dto.response.UserSearchResponse
import xquare.app.xquareinfra.domain.user.application.port.`in`.FindAllSearchUserUseCase

@RestController
@RequestMapping("/user")
class UserWebAdapter(
    private val findAllSearchUserUseCase: FindAllSearchUserUseCase
) {
    @GetMapping("/all")
    fun findAllUser(): List<UserSearchResponse> = findAllSearchUserUseCase.findAllSearchUser()
}