package xquare.app.xquareinfra.domain.team.adapter.dto.response

import java.time.LocalDateTime

data class DetailTeamResponse(
    val teamNameKo: String,
    val teamNameEn: String,
    val memberCount: Int,
    val adminName: String,
    val createdAt: LocalDateTime,
    val memberList: List<TeamMemberResponse>,
    val isAdmin: Boolean
)
