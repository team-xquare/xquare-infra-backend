package xquare.app.xquareinfra.domain.user.application.port.out

import xquare.app.xquareinfra.domain.user.domain.User
import java.util.*

interface FindUserPort {
    fun findById(userId: UUID): User?
    fun findByAccountId(accountId: String): User?
    fun findAll(): List<User>
}
