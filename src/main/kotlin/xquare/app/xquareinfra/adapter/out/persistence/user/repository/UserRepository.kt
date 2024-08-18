package xquare.app.xquareinfra.adapter.out.persistence.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.user.domain.User
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
    fun findByAccountId(accountId: String): User?
    fun existsByAccountId(accountId: String): Boolean
}