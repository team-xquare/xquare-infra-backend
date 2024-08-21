package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.user.model.User
import java.util.UUID

interface ExistsUserTeamPort {
    fun existsByTeamIdAndUser(teamId: UUID, user: User): Boolean
}