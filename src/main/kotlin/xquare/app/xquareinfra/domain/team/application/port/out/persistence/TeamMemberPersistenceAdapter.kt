package xquare.app.xquareinfra.domain.team.application.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.team.application.port.out.SaveTeamMemberPort
import xquare.app.xquareinfra.domain.team.application.port.out.persistence.repository.TeamMemberRepository
import xquare.app.xquareinfra.domain.team.domain.TeamMember

@Component
class TeamMemberPersistenceAdapter(
    private val teamMemberRepository: TeamMemberRepository
): SaveTeamMemberPort {
    override fun save(teamMember: TeamMember): TeamMember =
        teamMemberRepository.save(teamMember)
}