package xquare.app.xquareinfra.adapter.out.persistence.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.model.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

interface UserTeamRepository : JpaRepository<UserTeam, Long> {
    fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, user: User): Boolean

    fun findByTeamAndUser(teamJpaEntity: TeamJpaEntity, user: User): UserTeam?
}