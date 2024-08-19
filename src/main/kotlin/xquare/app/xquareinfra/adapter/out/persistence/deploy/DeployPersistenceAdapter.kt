package xquare.app.xquareinfra.adapter.out.persistence.deploy

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.deploy.port.out.saveDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.adapter.out.persistence.deploy.repository.DeployRepository
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.*

@Component
class DeployPersistenceAdapter(
    private val deployRepository: DeployRepository
): saveDeployPort, ExistDeployPort, FindDeployPort {
    override fun saveDeploy(deployJpaEntity: DeployJpaEntity): DeployJpaEntity = deployRepository.save(deployJpaEntity)
    override fun existByDeployName(deployName: String): Boolean = deployRepository.existsByDeployName(deployName)
    override fun findByDeployName(deployName: String): DeployJpaEntity? = deployRepository.findByDeployName(deployName)
    override fun findAllByTeam(team: Team): List<DeployJpaEntity> = deployRepository.findAllByTeam(team)
    override fun findById(deployId: UUID): DeployJpaEntity? = deployRepository.findByIdOrNull(deployId)
}