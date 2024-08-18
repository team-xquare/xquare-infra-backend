package xquare.app.xquareinfra.adapter.out.persistence.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.user.port.out.ExistsUserPort
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.application.user.port.out.SaveUserPort
import xquare.app.xquareinfra.adapter.out.persistence.user.repository.UserRepository
import xquare.app.xquareinfra.domain.user.domain.User
import java.util.*

@Component
class UserPersistenceAdapter(
    private val userRepository: UserRepository
): FindUserPort, ExistsUserPort, SaveUserPort {
    override fun findById(userId: UUID): User? = userRepository.findByIdOrNull(userId)
    override fun findByAccountId(accountId: String): User? = userRepository.findByAccountId(accountId)
    override fun findAll(): List<User> = userRepository.findAll()

    override fun existsById(userId: UUID): Boolean = userRepository.existsById(userId)
    override fun existsByAccountId(accountId: String): Boolean = userRepository.existsByAccountId(accountId)
    override fun saveUser(user: User): User = userRepository.save(user)
}