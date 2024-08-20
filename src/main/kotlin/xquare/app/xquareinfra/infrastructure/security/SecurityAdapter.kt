package xquare.app.xquareinfra.infrastructure.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.user.UserMapper
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.security.principle.CustomUserDetails

@Component
internal class SecurityAdapter(
    private val userMapper: UserMapper
): SecurityPort {
    override fun readCurrentUser(): User {
        return (SecurityContextHolder.getContext().authentication.principal as CustomUserDetails).user
    }
}
