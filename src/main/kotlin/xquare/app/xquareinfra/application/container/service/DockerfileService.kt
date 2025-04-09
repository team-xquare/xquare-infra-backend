package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateGradleConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeWithNginxConfigRequest
import xquare.app.xquareinfra.application.container.port.`in`.DockerfileUseCase
import xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.WriteValuesPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class DockerfileService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val createDockerfilePort: CreateDockerfilePort,
    private val writeValuesPort: WriteValuesPort,
) : DockerfileUseCase {
    override fun createGradleDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createGradleConfigRequest: CreateGradleConfigRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createGradleConfigRequest.buildDir = deploy.projectRootDir

        createDockerfilePort.createDockerfile(
            deployName = deploy.deployName,
            environment = containerEnvironment,
            dockerfileRequest = createGradleConfigRequest
        )
    }

    override fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxConfigRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createNodeDockerfileRequest.port = container.containerPort
        createNodeDockerfileRequest.buildDir = deploy.projectRootDir

        createDockerfilePort.createDockerfile(
            deployName = deploy.deployName,
            environment = containerEnvironment,
            dockerfileRequest = createNodeDockerfileRequest
        )
    }

    override fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeConfigRequest: CreateNodeConfigRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createNodeConfigRequest.port = container.containerPort
        createNodeConfigRequest.buildDir = deploy.projectRootDir

        createDockerfilePort.createDockerfile(
            deployName = deploy.deployName,
            environment = containerEnvironment,
            dockerfileRequest = createNodeConfigRequest
        )
    }
}