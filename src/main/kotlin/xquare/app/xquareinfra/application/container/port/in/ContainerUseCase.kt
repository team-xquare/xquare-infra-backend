package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.UpdateContainerWebhookRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface ContainerUseCase {
    fun getContainerByDeploy(deployId: UUID): List<SimpleContainerResponse>

    fun getContainerDetails(deployId: UUID, environment: ContainerEnvironment): GetContainerDetailsResponse

    fun syncContainer(syncContainerRequest: SyncContainerRequest)

    fun updateEnvironmentVariable(
        deployId: UUID,
        environment: ContainerEnvironment,
        environmentVariable: Map<String, String>
    )
    fun updateContainerWebhook(
        updateContainerWebhookRequest: UpdateContainerWebhookRequest,
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    )

    fun setContainerConfig(
        deployId: UUID,
        setContainerConfigRequest: SetContainerConfigRequest
    )

    fun syncContainerDomain(deployName: String, containerEnvironment: ContainerEnvironment, domain: String)

}