package xquare.app.xquareinfra.adapter.out.persistence.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import java.util.UUID

interface UserRepository: JpaRepository<UserJpaEntity, UUID> {
    fun findByAccountId(accountId: String): UserJpaEntity?
    fun existsByAccountId(accountId: String): Boolean
}