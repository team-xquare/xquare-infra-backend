package xquare.app.xquareinfra.adapter.`in`.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.user.dto.response.UserSearchResponse
import xquare.app.xquareinfra.application.user.port.`in`.FindAllSearchUserUseCase

@RestController
@RequestMapping("/v1/user")
class V1UserWebAdapter(
    private val findAllSearchUserUseCase: FindAllSearchUserUseCase
) {
    @GetMapping("/all")
    fun findAllUser(): List<UserSearchResponse> = findAllSearchUserUseCase.findAllSearchUser()
}