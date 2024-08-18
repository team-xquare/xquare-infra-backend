package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.domain.container.domain.WebhookType

data class UpdateContainerWebhookRequest(
    val webhookType: WebhookType,
    val webhookUrl: String
)
