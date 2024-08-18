package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.domain.Team

interface SaveTeamPort {
    fun save(team: Team): Team
}