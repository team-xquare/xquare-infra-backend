package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface GetContainerCpuUsageUseCase {
    fun getContainerCpuUsage(deployName: String, environment: ContainerEnvironment): MutableMap<String, Map<String, String>>
}