package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.adapter.`in`.container.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface ContainerDeployHistoryPort {
    fun getContainerDeployHistory(deployName: String, containerEnvironment: ContainerEnvironment): List<DeployHistoryResponse>
}