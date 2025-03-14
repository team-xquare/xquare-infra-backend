package xquare.app.xquareinfra.adapter.out.persistence.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.UUID

interface TeamRepository : JpaRepository<TeamJpaEntity, UUID> {
    fun existsByTeamNameEn(teamNameEn: String): Boolean
    fun findByTeamNameEn(teamNameEn: String): TeamJpaEntity?
}