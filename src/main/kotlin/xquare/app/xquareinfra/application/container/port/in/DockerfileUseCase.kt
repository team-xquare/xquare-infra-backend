package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateGradleDockerfileRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeWithNginxDockerfileRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface DockerfileUseCase {
    fun createGradleDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createGradleDockerfileRequest: CreateGradleDockerfileRequest
    )

    fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
    )

    fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    )
}