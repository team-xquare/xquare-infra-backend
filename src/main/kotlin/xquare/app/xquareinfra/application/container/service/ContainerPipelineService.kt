package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.adapter.out.external.gocd.client.GocdClient
import xquare.app.xquareinfra.application.container.port.`in`.ContainerPipelineUseCase
import xquare.app.xquareinfra.application.container.port.out.ContainerPipelinePort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class ContainerPipelineService(
    private val findDeployPort: FindDeployPort,
    private val containerPipelinePort: ContainerPipelinePort,
    private val gocdClient: GocdClient
) : ContainerPipelineUseCase {
    override fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val histories = containerPipelinePort.getContainerPipelineHistory(deploy.deployName, containerEnvironment)

        return GetContainerDeployHistoryResponse(histories)
    }

    override fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String {
        return gocdClient.getStageLog(pipelineName, pipelineCounter, stageName, "")
    }

    override fun schedulePipeline(deployId: UUID, containerEnvironment: ContainerEnvironment) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        containerPipelinePort.schedulePipeline(
            deployName = deploy.deployName,
            containerEnvironment = containerEnvironment
        )
    }
}