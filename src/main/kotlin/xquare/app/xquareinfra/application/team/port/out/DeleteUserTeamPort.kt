package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.user.model.User

interface DeleteUserTeamPort {
    fun deleteByUserAndTeam(user: User, team: Team)
}