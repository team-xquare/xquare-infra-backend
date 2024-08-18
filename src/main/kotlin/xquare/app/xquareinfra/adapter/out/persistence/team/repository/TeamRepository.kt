package xquare.app.xquareinfra.adapter.out.persistence.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface TeamRepository : JpaRepository<Team, UUID> {
    fun existsByTeamNameEn(teamNameEn: String): Boolean
    fun findByTeamNameEn(teamNameEn: String): Team?
}