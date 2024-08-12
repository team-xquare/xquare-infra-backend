package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.UpdateContainerWebhookRequest
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface UpdateContainerWebhookUseCase {
    fun updateContainerWebhook(updateContainerWebhookRequest: UpdateContainerWebhookRequest, deployId: UUID, containerEnvironment: ContainerEnvironment)
}