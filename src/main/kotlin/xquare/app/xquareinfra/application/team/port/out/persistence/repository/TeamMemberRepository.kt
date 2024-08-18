package xquare.app.xquareinfra.application.team.port.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.TeamMember
import java.util.UUID

interface TeamMemberRepository : JpaRepository<TeamMember, UUID> {
    fun existsByMemberIdAndTeam(memberId: UUID, team: Team): Boolean
}