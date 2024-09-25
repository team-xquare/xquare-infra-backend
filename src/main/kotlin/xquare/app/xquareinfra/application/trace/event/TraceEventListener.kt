package xquare.app.xquareinfra.application.trace.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.container.model.WebhookType
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.webhook.AlertMessageGenerator
import xquare.app.xquareinfra.infrastructure.webhook.discord.DiscordMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.slack.SlackMessageSender

@Component
class TraceEventListener(
    private val findContainerPort: FindContainerPort,
    private val findDeployPort: FindDeployPort,
    private val slackMessageSender: SlackMessageSender,
    private val discordMessageSender: DiscordMessageSender
) {
    @EventListener
    fun handleTraceEvent(event: SpanEvent) {
        val span = event.span

        if (span.isErrorSpan()) {
            val fullName = span.getServiceNameInScope() ?: throw IllegalArgumentException("Service Name is null")
            val containerInfo = ContainerUtil.getContainerInfoByFullName(fullName)

            val deploy = findDeployPort.findByDeployName(containerInfo.serviceName)
                ?: throw IllegalArgumentException("Deploy Not Found Exception")

            val container = findContainerPort.findByDeployAndEnvironment(deploy, containerInfo.containerEnvironment)
                ?: throw IllegalArgumentException("Container Not Found Exception")

            val errorMessage = AlertMessageGenerator.makeErrorMessage(span)

            val webhookInfo = container.webhookInfo ?: throw IllegalArgumentException("Webhook Info is null")

            when (webhookInfo.webhookType) {
                WebhookType.SLACK -> slackMessageSender.send(webhookInfo.webhookUrl, errorMessage)
                WebhookType.DISCORD -> discordMessageSender.send(webhookInfo.webhookUrl, errorMessage)
            }
        }
    }
}