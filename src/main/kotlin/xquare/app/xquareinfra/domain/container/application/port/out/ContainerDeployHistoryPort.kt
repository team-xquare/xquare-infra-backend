package xquare.app.xquareinfra.domain.container.application.port.out

import xquare.app.xquareinfra.domain.container.adapter.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface ContainerDeployHistoryPort {
    fun getContainerDeployHistory(deployName: String, containerEnvironment: ContainerEnvironment): List<DeployHistoryResponse>
}