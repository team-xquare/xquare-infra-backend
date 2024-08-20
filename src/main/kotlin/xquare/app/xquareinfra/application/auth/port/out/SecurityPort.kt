package xquare.app.xquareinfra.application.auth.port.out

import xquare.app.xquareinfra.domain.user.model.User

interface SecurityPort {
    fun readCurrentUser(): User
}
