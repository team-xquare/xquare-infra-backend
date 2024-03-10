package xquare.app.xquareinfra.domain.team.application.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.SaveTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.persistence.repository.TeamRepository
import xquare.app.xquareinfra.domain.team.domain.Team

@Component
class TeamPersistenceAdapter(
    private val teamRepository: TeamRepository
): SaveTeamPort, ExistsTeamPort {
    override fun save(team: Team): Team = teamRepository.save(team)
    override fun existsByTeamNameEn(teamNameEn: String): Boolean = teamRepository.existsByTeamNameEn(teamNameEn)
}