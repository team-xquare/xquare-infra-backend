package xquare.app.xquareinfra.adapter.out.persistence.deploy

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.deploy.port.out.SaveDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.adapter.out.persistence.deploy.repository.DeployRepository
import xquare.app.xquareinfra.adapter.out.persistence.team.TeamMapper
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.team.model.Team
import java.util.*

@Component
class DeployPersistenceAdapter(
    private val deployRepository: DeployRepository,
    private val deployMapper: DeployMapper,
    private val teamMapper: TeamMapper
): SaveDeployPort, ExistDeployPort, FindDeployPort {
    override fun saveDeploy(deploy: Deploy): Deploy {
        return deployMapper.toModel(deployRepository.save(deployMapper.toEntity(deploy)))
    }
    override fun existByDeployName(deployName: String): Boolean {
        return deployRepository.existsByDeployName(deployName)
    }
    override fun findByDeployName(deployName: String): Deploy? {
        return deployRepository.findByDeployName(deployName)?.let { deployMapper.toModel(it) }
    }
    override fun findAllByTeam(team: Team): List<Deploy> {
        return deployRepository.findAllByTeamId(teamMapper.toEntity(team).id!!).map { deployMapper.toModel(it) }
    }
    override fun findById(deployId: UUID): Deploy? {
        return deployRepository.findByIdOrNull(deployId)?.let { deployMapper.toModel(it) }
    }
}