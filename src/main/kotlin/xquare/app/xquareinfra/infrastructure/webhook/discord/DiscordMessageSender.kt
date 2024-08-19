package xquare.app.xquareinfra.infrastructure.webhook.discord

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.integration.resttemplate.RestTemplateHttpRequestSender
import xquare.app.xquareinfra.infrastructure.webhook.WebhookMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.discord.dto.request.SendDiscordMessageWebhookRequest

@Component
class DiscordMessageSender(
    private val restTemplateHttpRequestSender: RestTemplateHttpRequestSender
) : WebhookMessageSender{
    override fun send(webhookUrl: String, message: String) {
        restTemplateHttpRequestSender.requestPost(webhookUrl, SendDiscordMessageWebhookRequest(message))
    }
}