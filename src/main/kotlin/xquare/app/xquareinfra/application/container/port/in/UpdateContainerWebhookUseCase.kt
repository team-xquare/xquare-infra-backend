package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.UpdateContainerWebhookRequest
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface UpdateContainerWebhookUseCase {
    fun updateContainerWebhook(updateContainerWebhookRequest: UpdateContainerWebhookRequest, deployId: UUID, containerEnvironment: ContainerEnvironment)
}