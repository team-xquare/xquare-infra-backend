package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity

interface SaveTeamPort {
    fun save(teamJpaEntity: TeamJpaEntity): TeamJpaEntity
}