package xquare.app.xquareinfra.application.user.port.`in`

import xquare.app.xquareinfra.adapter.`in`.user.dto.response.UserSearchResponse

interface FindAllSearchUserUseCase {
    fun findAllSearchUser(): List<UserSearchResponse>
}