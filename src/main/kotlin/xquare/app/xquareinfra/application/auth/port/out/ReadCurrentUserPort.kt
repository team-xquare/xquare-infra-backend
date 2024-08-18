package xquare.app.xquareinfra.application.auth.port.out

import xquare.app.xquareinfra.domain.user.domain.User

interface ReadCurrentUserPort {
    fun readCurrentUser(): User
}
