package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest

interface CreateDockerfilePort {
    fun <T : DockerfileRequest> createDockerfile(
        deployName: String,
        environment: ContainerEnvironment,
        dockerfileRequest: T
    )
}