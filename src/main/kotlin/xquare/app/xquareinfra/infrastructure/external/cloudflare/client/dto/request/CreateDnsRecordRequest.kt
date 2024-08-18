package xquare.app.xquareinfra.infrastructure.external.cloudflare.client.dto.request

data class CreateDnsRecordRequest(
    val content: String,
    val name: String,
    val proxied: Boolean,
    val type: String
)
