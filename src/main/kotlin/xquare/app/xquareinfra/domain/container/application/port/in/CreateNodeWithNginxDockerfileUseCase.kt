package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeWithNginxDockerfileRequest
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface CreateNodeWithNginxDockerfileUseCase {
    fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    )
}