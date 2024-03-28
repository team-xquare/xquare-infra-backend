package xquare.app.xquareinfra.domain.team.adapter.dto.response

import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import java.util.UUID

data class TeamMemberResponse(
    val memberName: String,
    val memberNumber: String,
    val memberRole: TeamMemberRole,
    val userId: UUID
)
