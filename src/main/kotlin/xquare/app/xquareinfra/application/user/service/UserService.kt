package xquare.app.xquareinfra.application.user.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.user.dto.response.UserSearchResponse
import xquare.app.xquareinfra.application.user.port.`in`.UserUseCase
import xquare.app.xquareinfra.application.user.port.out.FindUserPort

@Service
class UserService(
    private val findUserPort: FindUserPort
): UserUseCase {
    override fun findAllSearchUser(): List<UserSearchResponse> {
        val users = findUserPort.findAll()
        return users.map {
            UserSearchResponse(
                userId = it.id!!,
                numberAndName = "${it.grade}${it.classNum}${String.format("%02d", it.number)} ${it.name}")
        }
    }
}