package xquare.app.xquareinfra.application.user.port.out

import xquare.app.xquareinfra.domain.user.domain.User

interface SaveUserPort {
    fun saveUser(user: User): User
}