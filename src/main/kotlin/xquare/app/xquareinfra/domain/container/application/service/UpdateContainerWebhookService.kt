package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.request.UpdateContainerWebhookRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.UpdateContainerWebhookUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class UpdateContainerWebhookService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort
): UpdateContainerWebhookUseCase {
    override fun updateContainerWebhook(
        updateContainerWebhookRequest: UpdateContainerWebhookRequest,
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        container.updateWebhookUrl(
            webhookUrl = updateContainerWebhookRequest.webhookUrl,
            webhookType = updateContainerWebhookRequest.webhookType
        )
    }
}