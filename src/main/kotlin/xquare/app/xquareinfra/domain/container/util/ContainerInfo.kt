package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment

data class ContainerInfo(
    val serviceName: String,
    val containerEnvironment: ContainerEnvironment
)
