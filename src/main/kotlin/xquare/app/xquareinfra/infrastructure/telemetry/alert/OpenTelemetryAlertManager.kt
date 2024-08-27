package xquare.app.xquareinfra.infrastructure.telemetry.alert

import io.opentelemetry.proto.trace.v1.Span
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.container.model.WebhookType
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.telemetry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.webhook.discord.DiscordMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.slack.SlackMessageSender

@Component
class OpenTelemetryAlertManager(
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val discordMessageSender: DiscordMessageSender,
    private val slackMessageSender: SlackMessageSender,
    private val findDeployPort: FindDeployPort
){
    fun notification(span: Span, analysisResult: AnalysisResult, serviceName: String?) {
        val containers = findContainerPort.findAll()

        val service = containers.find { container ->
            val deploy = findDeployPort.findById(container.deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
            listOf(
                "${deploy.deployName}-${deploy.deployType}-${container.containerEnvironment}",
                "${deploy.deployName}-${container.containerEnvironment}"
            ).any { it == serviceName }
        }

        service?.webhookInfo?.let {
            when(it.webhookType) {
                WebhookType.DISCORD -> discordMessageSender.send(it.webhookUrl, MessageGenerator.makeErrorMessage(span, analysisResult))
                WebhookType.SLACK -> slackMessageSender.send(it.webhookUrl, MessageGenerator.makeErrorMessage(span, analysisResult))
            }
        }
    }
}