package xquare.app.xquareinfra.domain.team.model

import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.model.User
import java.util.*

data class UserTeam(
    val id: Long? = null,
    val user: User,
    val team: Team,
    val role: TeamMemberRole
)
