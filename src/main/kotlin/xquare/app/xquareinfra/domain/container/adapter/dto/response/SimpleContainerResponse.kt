package xquare.app.xquareinfra.domain.container.adapter.dto.response

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.domain.ContainerStatus
import java.time.LocalDateTime

data class SimpleContainerResponse(
    val containerName: String,
    val containerEnvironment: ContainerEnvironment,
    val containerStatus: ContainerStatus,
    val repository: String,
    val domain: String,
    val lastDeploy: LocalDateTime
)
