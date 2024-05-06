package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface GetContainerLogUseCase {
    fun getContainerLog(deployName: String, environment: ContainerEnvironment): GetContainerLogResponse
}