package xquare.app.xquareinfra.domain.user.model

import xquare.app.xquareinfra.domain.team.model.UserTeam
import java.util.*

data class User(
    val id: UUID?,
    val name: String,
    val accountId: String,
    val grade: Int,
    val classNum: Int,
    val number: Int,
    val roles: List<Role>,
    val email: String,
    var teams: Set<UserTeam> = emptySet()
) {
    fun addTeam(team: UserTeam): User {
        return copy(teams = teams + team)
    }

    fun removeTeam(teamId: UUID): User {
        return copy(teams = teams.filter { it.teamId != teamId }.toSet())
    }

    fun updateRoles(newRoles: List<Role>): User {
        return copy(roles = newRoles)
    }
}
