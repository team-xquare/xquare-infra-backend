package xquare.app.xquareinfra.domain.team.application.port.out

import xquare.app.xquareinfra.domain.team.domain.Team

interface SaveTeamPort {
    fun save(team: Team): Team
}