package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.TeamRepository
import xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.application.team.port.out.SaveTeamPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.*

@Component
class TeamPersistenceAdapter(
    private val teamRepository: TeamRepository,
    private val teamMapper: TeamMapper
): SaveTeamPort, ExistsTeamPort, FindTeamPort {
    override fun save(team: Team): Team {
        return teamMapper.toModel(teamRepository.save(teamMapper.toEntity(team)))
    }
    override fun existsByTeamNameEn(teamNameEn: String): Boolean {
        return teamRepository.existsByTeamNameEn(teamNameEn)
    }
    override fun findById(teamId: UUID): Team? {
        return teamRepository.findByIdOrNull(teamId)?.let { teamMapper.toModel(it) }
    }
    override fun findByName(teamName: String): Team? {
        return teamRepository.findByTeamNameEn(teamName)?.let { teamMapper.toModel(it) }
    }
}