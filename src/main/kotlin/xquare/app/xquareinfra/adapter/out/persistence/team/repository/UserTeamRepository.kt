package xquare.app.xquareinfra.adapter.out.persistence.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

interface UserTeamRepository : JpaRepository<UserTeam, Long> {
    fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, userJpaEntity: UserJpaEntity): Boolean

    fun findByTeamAndUser(teamJpaEntity: TeamJpaEntity, userJpaEntity: UserJpaEntity): UserTeam?
}