package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface GetContainerDetailsUseCase {
    fun getContainerDetails(deployId: UUID, environment: ContainerEnvironment): GetContainerDetailsResponse
}