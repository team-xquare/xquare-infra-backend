package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface ExistsUserTeamPort {
    fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, userJpaEntity: UserJpaEntity): Boolean
}