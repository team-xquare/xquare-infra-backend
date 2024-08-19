package xquare.app.xquareinfra.adapter.out.external.gocd

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.StageStatus
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.adapter.out.external.gocd.client.GocdClient

@Component
class GocdAdapter(
    private val gocdClient: GocdClient
): xquare.app.xquareinfra.application.container.port.out.ContainerDeployHistoryPort {
    override fun getContainerDeployHistory(
        deployName: String,
        containerEnvironment: ContainerEnvironment
    ): List<DeployHistoryResponse> {
        val pipelineName = "build-$deployName-${containerEnvironment.name}"
        val histories = gocdClient.getPipelinesHistory(pipelineName, "application/vnd.go.cd.v1+json")

        if (histories.statusCode.is4xxClientError) {
            return emptyList()
        }

        return histories.body?.pipelines?.mapNotNull { pipeline ->
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
                    commitMessage = modification.comment ?: "",
                    pipelineCounter = pipeline.counter!!,
                    pipelineName = pipeline.name!!
                )
            }
        }?.sortedByDescending { it.scheduledDate } ?: emptyList()
    }
}