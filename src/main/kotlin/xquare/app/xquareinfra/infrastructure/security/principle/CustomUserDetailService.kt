package xquare.app.xquareinfra.infrastructure.security.principle

import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import java.util.*

@Component
class CustomUserDetailService(
    private val findUserPort: FindUserPort
) : UserDetailsService {
    override fun loadUserByUsername(userId: String?): CustomUserDetails {
        val user = findUserPort.findById(UUID.fromString(userId!!)) ?: throw BusinessLogicException.USER_NOT_FOUND

        return CustomUserDetails(user)
    }
}
