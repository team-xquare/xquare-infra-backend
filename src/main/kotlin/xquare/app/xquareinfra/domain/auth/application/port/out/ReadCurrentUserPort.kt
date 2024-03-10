package xquare.app.xquareinfra.domain.auth.application.port.out

import xquare.app.xquareinfra.domain.user.domain.User

interface ReadCurrentUserPort {
    fun readCurrentUser(): User
}
