package xquare.app.xquareinfra.application.container.port.out

interface ContainerDnsPort {
    fun createGatewayDnsRecords(name: String)
    fun listDnsRecords(): List<DnsRecord>
}

data class DnsRecord(val name: String, val content: String, val type: String)