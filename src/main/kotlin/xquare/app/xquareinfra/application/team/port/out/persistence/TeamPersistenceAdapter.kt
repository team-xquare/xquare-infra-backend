package xquare.app.xquareinfra.application.team.port.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.application.team.port.out.SaveTeamPort
import xquare.app.xquareinfra.application.team.port.out.persistence.repository.TeamRepository
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.*

@Component
class TeamPersistenceAdapter(
    private val teamRepository: xquare.app.xquareinfra.application.team.port.out.persistence.repository.TeamRepository
): xquare.app.xquareinfra.application.team.port.out.SaveTeamPort,
    xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort,
    xquare.app.xquareinfra.application.team.port.out.FindTeamPort {
    override fun save(team: Team): Team = teamRepository.save(team)
    override fun existsByTeamNameEn(teamNameEn: String): Boolean = teamRepository.existsByTeamNameEn(teamNameEn)
    override fun findById(teamId: UUID): Team? = teamRepository.findByIdOrNull(teamId)
    override fun findByName(teamName: String): Team? = teamRepository.findByTeamNameEn(teamName)
}