package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateGradleDockerfileRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface CreateGradleDockerfileUseCase {
    fun createGradleDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createGradleDockerfileRequest: CreateGradleDockerfileRequest
    )
}