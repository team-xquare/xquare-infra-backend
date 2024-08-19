package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface FindUserTeamPort {
    fun findByUserAndTeam(userJpaEntity: UserJpaEntity, teamJpaEntity: TeamJpaEntity): UserTeam?
}