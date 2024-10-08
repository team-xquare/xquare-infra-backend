package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity

interface FindUserTeamPort {
    fun findByUserAndTeam(user: User, team: Team): UserTeam?
    fun findAllByUser(user: User): List<UserTeam>
    fun findAllByTeam(team: Team): List<UserTeam>
}