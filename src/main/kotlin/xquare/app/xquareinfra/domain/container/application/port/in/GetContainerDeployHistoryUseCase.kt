package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface GetContainerDeployHistoryUseCase {
    fun getContainerDeployHistory(deployId: UUID, containerEnvironment: ContainerEnvironment): GetContainerDeployHistoryResponse
}