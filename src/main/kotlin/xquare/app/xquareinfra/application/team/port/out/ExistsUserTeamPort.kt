package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface ExistsUserTeamPort {
    fun existsByTeamAndUser(team: Team, user: User): Boolean
}