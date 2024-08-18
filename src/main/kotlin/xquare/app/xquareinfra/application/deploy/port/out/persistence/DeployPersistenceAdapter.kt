package xquare.app.xquareinfra.application.deploy.port.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.deploy.port.out.saveDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.persistence.repository.DeployRepository
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.*

@Component
class DeployPersistenceAdapter(
    private val deployRepository: DeployRepository
): saveDeployPort, ExistDeployPort, FindDeployPort {
    override fun saveDeploy(deploy: Deploy): Deploy = deployRepository.save(deploy)
    override fun existByDeployName(deployName: String): Boolean = deployRepository.existsByDeployName(deployName)
    override fun findByDeployName(deployName: String): Deploy? = deployRepository.findByDeployName(deployName)
    override fun findAllByTeam(team: Team): List<Deploy> = deployRepository.findAllByTeam(team)
    override fun findById(deployId: UUID): Deploy? = deployRepository.findByIdOrNull(deployId)
}