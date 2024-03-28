package xquare.app.xquareinfra.domain.team.adapter.dto.response

import xquare.app.xquareinfra.domain.team.domain.type.TeamType
import java.util.UUID

data class SimpleTeamResponse(
    val teamId: UUID,
    val teamNameKo: String,
    val teamNameEn: String,
    val administratorName: String,
    val teamType: TeamType
)
