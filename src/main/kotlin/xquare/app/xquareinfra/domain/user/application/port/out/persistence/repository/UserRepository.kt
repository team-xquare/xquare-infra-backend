package xquare.app.xquareinfra.domain.user.application.port.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.user.domain.User
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
}