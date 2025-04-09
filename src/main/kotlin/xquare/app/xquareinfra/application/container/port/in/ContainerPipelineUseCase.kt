package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface ContainerPipelineUseCase {
    fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse

    fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String

    fun schedulePipeline(deployId: UUID, containerEnvironment: ContainerEnvironment)
}
