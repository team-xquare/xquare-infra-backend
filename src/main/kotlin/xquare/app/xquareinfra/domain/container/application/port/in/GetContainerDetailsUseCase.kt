package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface GetContainerDetailsUseCase {
    fun getContainerDetails(deployId: UUID, environment: ContainerEnvironment): GetContainerDetailsResponse
}