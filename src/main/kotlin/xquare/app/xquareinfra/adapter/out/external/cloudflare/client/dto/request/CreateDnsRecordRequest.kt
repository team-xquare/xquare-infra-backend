package xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.request

data class CreateDnsRecordRequest(
    val content: String,
    val name: String,
    val proxied: Boolean,
    val type: String
)
