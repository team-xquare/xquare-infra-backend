package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface GetContainerLogUseCase {
    fun getContainerLog(deployId: UUID, environment: ContainerEnvironment): GetContainerLogResponse
}