package xquare.app.xquareinfra.infrastructure.opentelemtry.alert

import io.opentelemetry.proto.trace.v1.Span
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.WebhookType
import xquare.app.xquareinfra.infrastructure.opentelemtry.analyze.AnalysisResult
import xquare.app.xquareinfra.infrastructure.webhook.discord.DiscordMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.slack.SlackMessageSender

@Component
class OpenTelemetryAlertManager(
    private val findContainerPort: FindContainerPort,
    private val discordMessageSender: DiscordMessageSender,
    private val slackMessageSender: SlackMessageSender
){
    fun notification(span: Span, analysisResult: AnalysisResult, serviceName: String?) {
        val containers = findContainerPort.findAll()
        val service = containers.find { container ->
            listOf(
                "${container.deploy.deployName}-${container.deploy.deployType}-${container.containerEnvironment}",
                "${container.deploy.deployName}-${container.containerEnvironment}"
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