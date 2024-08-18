package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateGradleDockerfileRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.CreateGradleDockerfileUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class CreateGradleDockerfileService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val createDockerfilePort: CreateDockerfilePort
) : CreateGradleDockerfileUseCase {
    override fun createGradleDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createGradleDockerfileRequest: CreateGradleDockerfileRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createDockerfilePort.createDockerfile(
            deployName = deploy.deployName,
            environment = containerEnvironment,
            dockerfileRequest = createGradleDockerfileRequest
        )
    }
}