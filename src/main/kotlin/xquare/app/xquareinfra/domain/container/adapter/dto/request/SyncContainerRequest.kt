package xquare.app.xquareinfra.domain.container.adapter.dto.request

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

data class SyncContainerRequest(
    val deployName: String,
    val containerEnvironment: ContainerEnvironment,
    val subDomain: String?
)
