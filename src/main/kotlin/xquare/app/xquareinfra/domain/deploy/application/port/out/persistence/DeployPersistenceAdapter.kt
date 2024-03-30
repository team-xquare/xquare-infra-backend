package xquare.app.xquareinfra.domain.deploy.application.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.deploy.application.port.out.CreateDeployPort
import xquare.app.xquareinfra.domain.deploy.application.port.out.ExistDeployPort
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.application.port.out.persistence.repository.DeployRepository
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.domain.Team

@Component
class DeployPersistenceAdapter(
    private val deployRepository: DeployRepository
): CreateDeployPort, ExistDeployPort, FindDeployPort {
    override fun createDeploy(deploy: Deploy): Deploy = deployRepository.save(deploy)
    override fun existByDeployName(deployName: String): Boolean = deployRepository.existsByDeployName(deployName)
    override fun findByDeployName(deployName: String): Deploy? = deployRepository.findByDeployName(deployName)
    override fun findAllByTeam(team: Team): List<Deploy> = deployRepository.findAllByTeam(team)
}