package xquare.app.xquareinfra.application.team.port.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

interface UserTeamRepository : JpaRepository<UserTeam, Long> {
    fun existsByTeamAndUser(team: Team, user: User): Boolean

    fun findByTeamAndUser(team: Team, user: User): UserTeam?
}