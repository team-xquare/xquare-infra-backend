package xquare.app.xquareinfra.infrastructure.security.principle

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

class CustomUserDetails(
    val userJpaEntity: UserJpaEntity
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        userJpaEntity.roles.map {
            SimpleGrantedAuthority(it.name)
        }.toMutableList()

    override fun getPassword(): String? = null

    override fun getUsername(): String = userJpaEntity.accountId

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
