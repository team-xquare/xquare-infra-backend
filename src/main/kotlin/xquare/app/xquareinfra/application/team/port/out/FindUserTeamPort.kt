package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.model.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

interface FindUserTeamPort {
    fun findByUserAndTeam(user: User, teamJpaEntity: TeamJpaEntity): UserTeam?
}