package xquare.app.xquareinfra.domain.container.model

import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class WebhookInfo(
    @Enumerated(EnumType.STRING)
    val webhookType: WebhookType,
    val webhookUrl: String
)