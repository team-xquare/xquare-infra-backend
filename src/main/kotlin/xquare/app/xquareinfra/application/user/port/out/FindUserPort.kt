package xquare.app.xquareinfra.application.user.port.out

import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import java.util.*

interface FindUserPort {
    fun findById(userId: UUID): UserJpaEntity?
    fun findByAccountId(accountId: String): UserJpaEntity?
    fun findAll(): List<UserJpaEntity>
}
