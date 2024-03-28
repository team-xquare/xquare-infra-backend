package xquare.app.xquareinfra.infrastructure.auth

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.user.domain.User
import xquare.app.xquareinfra.infrastructure.security.principle.CustomUserDetails

@Component
internal class ReadCurrentUserAdapter : ReadCurrentUserPort {
    override fun readCurrentUser(): User =
        (SecurityContextHolder.getContext().authentication.principal as CustomUserDetails).user
}
