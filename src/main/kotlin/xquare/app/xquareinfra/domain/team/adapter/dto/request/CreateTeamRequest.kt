package xquare.app.xquareinfra.domain.team.adapter.dto.request

import xquare.app.xquareinfra.domain.team.domain.type.TeamType
import java.util.UUID

data class CreateTeamRequest(
    val teamNameKo: String,
    val teamNameEn: String,
    val teamType: TeamType,
    val teamMemberList: MutableList<UUID>
)
