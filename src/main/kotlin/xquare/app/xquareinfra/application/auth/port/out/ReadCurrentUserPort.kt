package xquare.app.xquareinfra.application.auth.port.out

import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface ReadCurrentUserPort {
    fun readCurrentUser(): UserJpaEntity
}
