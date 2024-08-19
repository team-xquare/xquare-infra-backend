package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team

interface SaveTeamPort {
    fun save(team: Team): Team
}