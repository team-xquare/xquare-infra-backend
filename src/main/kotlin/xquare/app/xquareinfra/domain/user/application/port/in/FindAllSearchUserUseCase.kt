package xquare.app.xquareinfra.domain.user.application.port.`in`

import xquare.app.xquareinfra.domain.user.adapter.dto.response.UserSearchResponse

interface FindAllSearchUserUseCase {
    fun findAllSearchUser(): List<UserSearchResponse>
}