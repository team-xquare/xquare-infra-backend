package xquare.app.xquareinfra.adapter.`in`.team.dto.request

import xquare.app.xquareinfra.domain.model.domain.type.TeamType
import java.util.UUID

data class CreateTeamRequest(
    val teamNameKo: String,
    val teamNameEn: String,
    val teamType: TeamType,
    val teamMemberList: MutableList<UUID>
)
