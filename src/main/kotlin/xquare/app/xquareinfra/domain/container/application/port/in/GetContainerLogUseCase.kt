package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface GetContainerLogUseCase {
    fun getContainerLog(deployId: UUID, environment: ContainerEnvironment): GetContainerLogResponse
}