package xquare.app.xquareinfra.domain.team.model

import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import java.util.*

data class UserTeam(
    val id: Long? = null,
    val userId: UUID,
    val teamId: UUID,
    val role: TeamMemberRole
)
