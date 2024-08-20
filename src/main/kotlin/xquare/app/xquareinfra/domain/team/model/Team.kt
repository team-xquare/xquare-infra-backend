package xquare.app.xquareinfra.domain.team.model

import xquare.app.xquareinfra.domain.team.model.type.TeamType
import xquare.app.xquareinfra.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

data class Team(
    val id: UUID?,
    val adminId: UUID,
    val teamNameKo: String,
    val teamNameEn: String,
    val teamType: TeamType,
    val createdAt: LocalDateTime = LocalDateTime.now()
)