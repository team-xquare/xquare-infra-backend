package xquare.app.xquareinfra.adapter.`in`.team.dto.response

import xquare.app.xquareinfra.domain.model.domain.type.TeamType
import java.util.UUID

data class SimpleTeamResponse(
    val teamId: UUID,
    val teamNameKo: String,
    val teamNameEn: String,
    val administratorName: String,
    val teamType: TeamType,
    val deployList: List<String>
)
