package xquare.app.xquareinfra.domain.team.model

import xquare.app.xquareinfra.domain.team.model.type.TeamType
import java.time.LocalDateTime
import java.util.*

data class Team(
    val id: UUID?,
    val adminId: UUID,
    val teamNameKo: String,
    val teamNameEn: String,
    val teamType: TeamType,
    var members: Set<UserTeam> = emptySet(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun addMember(userTeam: UserTeam): Team {
        return copy(members = members + userTeam)
    }

    fun removeMember(userId: UUID): Team {
        return copy(members = members.filter { it.userId != userId }.toSet())
    }
}