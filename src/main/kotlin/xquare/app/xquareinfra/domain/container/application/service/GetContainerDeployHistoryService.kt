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
import xquare.app.xquareinfra.infrastructure.feign.client.gocd.GocdClient
import java.util.*

@Transactional
@Service
class GetContainerDeployHistoryService(
    private val findDeployPort: FindDeployPort,
    private val gocdClient: GocdClient
) : GetContainerDeployHistoryUseCase{
    override fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val histories = gocdClient.getPipelinesHistory("build-${deploy.deployName}-${containerEnvironment.name}", "application/vnd.go.cd.v1+json")

        val response = histories.pipelines.map {
            val splitedNameAndEmail = it.buildCause.materialRevisions[0].modifications.get(0).userName.split(" ")
            DeployHistoryResponse(
                name = splitedNameAndEmail.get(0),
                email = splitedNameAndEmail.get(1),
                scheduledDate = it.scheduledDate,
                stages = it.stages.map {
                    StageStatus(
                        name = it.name,
                        status = it.status
                    )
                }.toList(),
                commitMessage = it.buildCause.materialRevisions[0].modifications.get(0).comment
            )
        }.sortedByDescending { it.scheduledDate }.toList()

        return GetContainerDeployHistoryResponse(response)
    }
}