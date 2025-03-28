package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateGradleConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeWithNginxConfigRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface DockerfileUseCase {
    fun createGradleDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createGradleConfigRequest: CreateGradleConfigRequest
    )

    fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeConfigRequest: CreateNodeConfigRequest
    )

    fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxConfigRequest
    )
}