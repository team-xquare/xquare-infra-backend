package xquare.app.xquareinfra.application.user.port.out

import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import java.util.*

interface FindUserPort {
    fun findById(userId: UUID): User?
    fun findByAccountId(accountId: String): User?
    fun findAll(): List<User>
}
