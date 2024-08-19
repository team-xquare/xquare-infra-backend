package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeWithNginxDockerfileRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface CreateNodeWithNginxDockerfileUseCase {
    fun createNodeWithNginxDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    )
}