package xquare.app.xquareinfra.infrastructure.external.client.cloudflare.dto.request

data class CreateDnsRecordRequest(
    val content: String,
    val name: String,
    val proxied: Boolean,
    val type: String
)
