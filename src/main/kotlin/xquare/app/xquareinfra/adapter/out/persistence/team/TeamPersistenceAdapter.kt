package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.TeamRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.*

@Component
class TeamPersistenceAdapter(
    private val teamRepository: TeamRepository
): xquare.app.xquareinfra.application.team.port.out.SaveTeamPort,
    xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort,
    xquare.app.xquareinfra.application.team.port.out.FindTeamPort {
    override fun save(teamJpaEntity: TeamJpaEntity): TeamJpaEntity = teamRepository.save(teamJpaEntity)
    override fun existsByTeamNameEn(teamNameEn: String): Boolean = teamRepository.existsByTeamNameEn(teamNameEn)
    override fun findById(teamId: UUID): TeamJpaEntity? = teamRepository.findByIdOrNull(teamId)
    override fun findByName(teamName: String): TeamJpaEntity? = teamRepository.findByTeamNameEn(teamName)
}