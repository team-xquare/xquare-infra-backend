package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam

interface SaveTeamPort {
    fun save(team: Team): Team
    fun saveTeamWithMembers(team: Team, members: List<UserTeam>): Team
}