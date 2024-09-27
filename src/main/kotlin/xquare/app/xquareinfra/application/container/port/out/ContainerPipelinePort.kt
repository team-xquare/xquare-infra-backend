package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.adapter.`in`.container.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment

interface ContainerPipelinePort {
    fun getContainerPipelineHistory(
        deployName: String,
        containerEnvironment: ContainerEnvironment
    ): List<DeployHistoryResponse>

    fun schedulePipeline(
        deployName: String,
        containerEnvironment: ContainerEnvironment
    )
}