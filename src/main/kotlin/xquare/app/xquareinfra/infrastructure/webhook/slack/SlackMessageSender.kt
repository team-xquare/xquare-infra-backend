package xquare.app.xquareinfra.infrastructure.webhook.slack

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.external.resttemplate.RestTemplateHttpRequestSender
import xquare.app.xquareinfra.infrastructure.webhook.WebhookMessageSender
import xquare.app.xquareinfra.infrastructure.webhook.slack.dto.request.SendSlackMessageWebhookRequest

@Component
class SlackMessageSender(
    private val restTemplateHttpRequestSender: RestTemplateHttpRequestSender
) : WebhookMessageSender {
    override fun send(webhookUrl: String, message: String) {
        restTemplateHttpRequestSender.requestPost(webhookUrl, SendSlackMessageWebhookRequest(message))
    }
}