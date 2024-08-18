package xquare.app.xquareinfra.infrastructure.auth.adapter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.user.domain.User
import xquare.app.xquareinfra.infrastructure.security.principle.CustomUserDetails

@Component
internal class ReadCurrentUserAdapter : ReadCurrentUserPort {
    override fun readCurrentUser(): User =
        (SecurityContextHolder.getContext().authentication.principal as CustomUserDetails).user
}
