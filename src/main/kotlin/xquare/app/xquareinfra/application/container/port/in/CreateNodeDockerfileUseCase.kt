package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface CreateNodeDockerfileUseCase {
    fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
    )
}