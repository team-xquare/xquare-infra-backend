package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.response.ListDnsRecordsResponse

interface ContainerDnsPort {
    fun createGatewayDnsRecords(name: String)
    fun listDnsRecords(): ListDnsRecordsResponse
}