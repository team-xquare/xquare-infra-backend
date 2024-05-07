package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerByDeployIdUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.domain.ContainerStatus
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class GetContainerByDeployIdService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort
): GetContainerByDeployIdUseCase {
    override fun getContainerByDeploy(deployId: UUID): List<SimpleContainerResponse> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        return containers.map {
            SimpleContainerResponse(
                containerName = deploy.deployName,
                containerEnvironment = it.containerEnvironment,
                containerStatus = ContainerStatus.RUNNING,
                repository = "${deploy.organization}/${deploy.repository}",
                domain = generateDomain(it),
                lastDeploy = it.lastDeploy
            )
        }
    }

    private fun generateDomain(container: Container): String {
        if(container.subDomain!!.isEmpty()) {
            val baseDomain = when (container.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${container.deploy.deployName}"
        }
        return container.subDomain!!
    }
}