package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.CreateNodeDockerfileUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class CreateNodeDockerfileService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val createDockerfilePort: CreateDockerfilePort
) : CreateNodeDockerfileUseCase {
    override fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
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