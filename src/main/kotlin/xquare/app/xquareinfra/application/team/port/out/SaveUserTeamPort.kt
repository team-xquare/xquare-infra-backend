package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.UserTeam

interface SaveUserTeamPort {
    fun saveUserTeam(userTeam: UserTeam): UserTeam
}