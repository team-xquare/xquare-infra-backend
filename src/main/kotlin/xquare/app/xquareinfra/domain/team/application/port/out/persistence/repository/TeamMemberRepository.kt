package xquare.app.xquareinfra.domain.team.application.port.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.team.domain.TeamMember
import java.util.UUID

interface TeamMemberRepository : JpaRepository<TeamMember, UUID> {
}