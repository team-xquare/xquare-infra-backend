package xquare.app.xquareinfra.domain.team.application.port.out

import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

interface FindUserTeamPort {
    fun findByUserAndTeam(user: User, team: Team): UserTeam?
}