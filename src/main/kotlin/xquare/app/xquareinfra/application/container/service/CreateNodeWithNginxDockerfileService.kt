package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeWithNginxDockerfileRequest
import xquare.app.xquareinfra.application.container.port.`in`.CreateNodeWithNginxDockerfileUseCase
import xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class CreateNodeWithNginxDockerfileService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val createDockerfilePort: xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
) : xquare.app.xquareinfra.application.container.port.`in`.CreateNodeWithNginxDockerfileUseCase {
    override fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createNodeDockerfileRequest.port = container.containerPort

        createDockerfilePort.createDockerfile(
            deployName = deploy.deployName,
            environment = containerEnvironment,
            dockerfileRequest = createNodeDockerfileRequest
        )
    }
}