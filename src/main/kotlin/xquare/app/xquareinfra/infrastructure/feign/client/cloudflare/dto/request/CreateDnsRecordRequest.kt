package xquare.app.xquareinfra.infrastructure.feign.client.cloudflare.dto.request

data class CreateDnsRecordRequest(
    val content: String,
    val name: String,
    val proxied: Boolean,
    val type: String
)
