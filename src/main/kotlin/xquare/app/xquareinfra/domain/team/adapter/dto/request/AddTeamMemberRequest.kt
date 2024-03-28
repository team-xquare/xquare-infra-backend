package xquare.app.xquareinfra.domain.team.adapter.dto.request

import java.util.UUID

data class AddTeamMemberRequest(
    val members: List<UUID>
)