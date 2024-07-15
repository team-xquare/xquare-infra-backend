package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface CreateNodeDockerfileUseCase {
    fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
    )
}