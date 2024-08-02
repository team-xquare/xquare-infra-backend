package xquare.app.xquareinfra.infrastructure.webhook

interface WebhookMessageSender {
    fun send(webhookUrl: String, message: String)
}