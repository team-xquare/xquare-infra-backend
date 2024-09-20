package xquare.app.xquareinfra.application.trace.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.container.model.WebhookType
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.webhook.slack.SlackMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.slack.dto.request.SendSlackMessageWebhookRequest

@Component
class TraceEventListener(
    private val findContainerPort: FindContainerPort,
    private val findDeployPort: FindDeployPort,
    private val slackMessageSender: SlackMessageSender
) {
    @EventListener
    fun handleTraceEvent(event: TraceEvent) {
        val trace = event.trace
        if(trace.isError()) {
            val fullName = trace.serviceName
            val containerInfo = fullName?.let { ContainerUtil.getContainerInfoByFullName(fullName) } ?: throw IllegalArgumentException("containerInfo is null")

            val deploy = findDeployPort.findByDeployName(containerInfo.serviceName) ?: throw IllegalArgumentException("Deploy Not Found Exception")
            val container = findContainerPort.findByDeployAndEnvironment(deploy, containerInfo.containerEnvironment)
                ?: throw IllegalArgumentException("Container Not Found Exception")

            when (container.webhookInfo?.webhookType) {
                WebhookType.SLACK -> slackMessageSender.send(container.webhookInfo.webhookUrl, )
            }
        }
    }
}