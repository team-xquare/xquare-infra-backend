package xquare.app.xquareinfra.infrastructure.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import xquare.app.xquareinfra.infrastructure.security.principle.CustomUserDetails

@Component
internal class ReadCurrentUserAdapter : ReadCurrentUserPort {
    override fun readCurrentUser(): UserJpaEntity =
        (SecurityContextHolder.getContext().authentication.principal as CustomUserDetails).userJpaEntity
}
