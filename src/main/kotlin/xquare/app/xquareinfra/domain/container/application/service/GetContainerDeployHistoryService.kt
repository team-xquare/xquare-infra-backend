package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.adapter.dto.response.StageStatus
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerDeployHistoryUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.external.client.gocd.GocdClient
import java.util.*

@Transactional
@Service
class GetContainerDeployHistoryService(
    private val findDeployPort: FindDeployPort,
    private val gocdClient: GocdClient
) : GetContainerDeployHistoryUseCase {
    override fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val histories = gocdClient.getPipelinesHistory("build-${deploy.deployName}-${containerEnvironment.name}", "application/vnd.go.cd.v1+json")

        val response = histories.pipelines?.mapNotNull { pipeline ->
            pipeline.buildCause?.materialRevisions?.firstOrNull()?.modifications?.firstOrNull()?.let { modification ->
                val splitNameAndEmail = modification.userName?.split(" ") ?: listOf("", "")
                DeployHistoryResponse(
                    name = splitNameAndEmail.getOrNull(0) ?: "",
                    email = splitNameAndEmail.getOrNull(1) ?: "",
                    scheduledDate = pipeline.scheduledDate ?: 0L,
                    stages = pipeline.stages?.mapNotNull { stage ->
                        stage.name?.let { name ->
                            StageStatus(
                                name = name,
                                status = stage.status ?: ""
                            )
                        }
                    } ?: emptyList(),
                    commitMessage = modification.comment ?: ""
                )
            }
        }?.sortedByDescending { it.scheduledDate } ?: emptyList()

        return GetContainerDeployHistoryResponse(response)
    }
}