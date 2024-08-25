package xquare.app.xquareinfra.adapter.out.external.cloudflare.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.request.CreateDnsRecordRequest
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.response.ListDnsRecordsResponse
import xquare.app.xquareinfra.adapter.out.external.feign.config.FeignConfig

@FeignClient(
    name = "cloudflare-client",
    url = "\${url.cloudflare}",
    configuration = [FeignConfig::class]
)
interface CloudflareClient {
    @GetMapping("/zones/{zone_id}/dns_records")
    fun listDnsRecords(
        @PathVariable("zone_id") zoneId: String,
        @RequestHeader("X-Auth-Email") xAuthEmail: String,
        @RequestHeader("X-Auth-Key") xAuthKey: String,
    ): ListDnsRecordsResponse

    @PostMapping("/zones/{zone_id}/dns_records")
    fun createDnsRecords(
        @PathVariable("zone_id") zoneId: String,
        @RequestHeader("X-Auth-Email") xAuthEmail: String,
        @RequestHeader("X-Auth-Key") xAuthKey: String,
        @RequestBody createDnsRecordRequest: CreateDnsRecordRequest
    )
}