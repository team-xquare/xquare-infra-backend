package xquare.app.xquareinfra.adapter.out.external.cloudflare

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.CloudflareClient
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.DnsType
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.request.CreateDnsRecordRequest
import xquare.app.xquareinfra.application.container.port.out.ContainerDnsPort
import xquare.app.xquareinfra.application.container.port.out.DnsRecord
import xquare.app.xquareinfra.infrastructure.env.cloudflare.CloudflareProperties
import xquare.app.xquareinfra.infrastructure.env.kubernetes.XquareProperties
import xquare.app.xquareinfra.infrastructure.exception.FeignException
import xquare.app.xquareinfra.adapter.out.external.Result

@Component
class CloudflareAdapter(
    private val xquareProperties: XquareProperties,
    private val cloudflareProperties: CloudflareProperties,
    private val cloudflareClient: CloudflareClient
) : ContainerDnsPort {
    override fun createGatewayDnsRecords(name: String) {
        val response = cloudflareClient.createDnsRecords(
            zoneId = cloudflareProperties.zoneId,
            xAuthEmail = cloudflareProperties.xAuthEmail,
            xAuthKey = cloudflareProperties.xAuthKey,
            createDnsRecordRequest = CreateDnsRecordRequest(
                content = xquareProperties.gatewayDns,
                name = name,
                proxied = false,
                type = DnsType.CNAME.name
            )
        )

        return response
    }

    override fun listDnsRecords(): List<DnsRecord> {
        val response = cloudflareClient.listDnsRecords(
            zoneId = cloudflareProperties.zoneId,
            xAuthEmail = cloudflareProperties.xAuthEmail,
            xAuthKey = cloudflareProperties.xAuthKey,
        )
        return response.result.map { DnsRecord(name = it.name, content = it.content, type = it.type) }
    }
}