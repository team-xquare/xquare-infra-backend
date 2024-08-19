package xquare.app.xquareinfra.adapter.out.persistence.team.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.team.model.TeamMember
import java.util.UUID

interface TeamMemberRepository : JpaRepository<TeamMember, UUID> {
    fun existsByMemberIdAndTeam(memberId: UUID, teamJpaEntity: TeamJpaEntity): Boolean
}