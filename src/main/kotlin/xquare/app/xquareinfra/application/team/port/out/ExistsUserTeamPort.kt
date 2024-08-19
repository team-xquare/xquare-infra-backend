package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.user.domain.User

interface ExistsUserTeamPort {
    fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, user: User): Boolean
}