package xquare.app.xquareinfra.adapter.`in`.team.dto.request

import java.util.UUID

data class AddTeamMemberRequest(
    val members: List<UUID>
)