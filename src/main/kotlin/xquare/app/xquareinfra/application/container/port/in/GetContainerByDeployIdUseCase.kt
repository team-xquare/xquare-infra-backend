package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.response.SimpleContainerResponse
import java.util.UUID

interface GetContainerByDeployIdUseCase {
    fun getContainerByDeploy(deployId: UUID): List<SimpleContainerResponse>
}