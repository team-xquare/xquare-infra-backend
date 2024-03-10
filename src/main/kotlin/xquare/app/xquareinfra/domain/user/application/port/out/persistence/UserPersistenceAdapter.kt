package xquare.app.xquareinfra.domain.user.application.port.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.domain.user.application.port.out.persistence.repository.UserRepository
import xquare.app.xquareinfra.domain.user.domain.User
import java.util.*

@Component
class UserPersistenceAdapter(
    private val userRepository: UserRepository
): FindUserPort {
    override fun findById(userId: UUID): User? = userRepository.findByIdOrNull(userId)
}