package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.user.domain.User

interface ExistsUserTeamPort {
    fun existsByTeamAndUser(team: Team, user: User): Boolean
}