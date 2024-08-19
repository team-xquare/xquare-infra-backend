package xquare.app.xquareinfra.application.user.port.out

import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface SaveUserPort {
    fun saveUser(userJpaEntity: UserJpaEntity): UserJpaEntity
}