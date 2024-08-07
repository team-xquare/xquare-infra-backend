package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.application.port.`in`.SyncContainerDomainUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.external.client.cloudflare.CloudflareClient
import xquare.app.xquareinfra.infrastructure.external.client.cloudflare.dto.request.CreateDnsRecordRequest
import xquare.app.xquareinfra.infrastructure.global.env.cloudflare.CloudflareProperties
import xquare.app.xquareinfra.infrastructure.kubernetes.env.XquareProperties

@Transactional
@Service
class SyncContainerDomainService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val cloudflareClient: CloudflareClient,
    private val cloudflareProperties: CloudflareProperties,
    private val xquareProperties: XquareProperties
) : SyncContainerDomainUseCase{
    override fun syncContainerDomain(
        deployName: String,
        containerEnvironment: ContainerEnvironment,
        domain: String
    ) {
        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment) ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        container.updateDomain(domain)

        val listResponse = cloudflareClient.listDnsRecords(
            cloudflareProperties.zoneId,
            cloudflareProperties.xAuthEmail,
            cloudflareProperties.xAuthKey
        )

        if(listResponse.statusCode.isError) {
            throw XquareException.INTERNAL_SERVER_ERROR
        }

        val records = listResponse.body
        if(!records!!.result.any { it.name == domain }) {
            val createResponse = cloudflareClient.createDnsRecords(
                cloudflareProperties.zoneId,
                cloudflareProperties.xAuthEmail,
                cloudflareProperties.xAuthKey,
                CreateDnsRecordRequest(
                    content = xquareProperties.gatewayDns,
                    name = domain,
                    proxied = false,
                    type = "CNAME"
                )
            )

            if(createResponse.statusCode.isError) {
                throw XquareException.INTERNAL_SERVER_ERROR
            }
        }
    }
}