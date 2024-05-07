package xquare.app.xquareinfra.domain.user.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.user.adapter.dto.response.UserSearchResponse
import xquare.app.xquareinfra.domain.user.application.port.`in`.FindAllSearchUserUseCase
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort

@Service
class FindAllSearchUserService(
    private val findUserPort: FindUserPort
): FindAllSearchUserUseCase {
    override fun findAllSearchUser(): List<UserSearchResponse> {
        val users = findUserPort.findAll()
        return users.map {
            UserSearchResponse(
                userId = it.id!!,
                numberAndName = "${it.grade}${it.classNum}${String.format("%02d", it.number)} ${it.name}")
        }
    }
}