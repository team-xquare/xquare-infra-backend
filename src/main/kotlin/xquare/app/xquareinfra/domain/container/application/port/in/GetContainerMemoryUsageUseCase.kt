package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface GetContainerMemoryUsageUseCase {
    fun getContainerMemoryUsageUseCase(deployName: String, environment: ContainerEnvironment): MutableMap<String, Map<String, String>>
}