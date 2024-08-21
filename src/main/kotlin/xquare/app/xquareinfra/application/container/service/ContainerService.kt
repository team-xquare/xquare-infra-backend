package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.ContainerConfigDetails
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.UpdateContainerWebhookRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.CloudflareClient
import xquare.app.xquareinfra.adapter.out.external.cloudflare.client.dto.request.CreateDnsRecordRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.GithubClient
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.adapter.out.external.github.env.GithubProperties
import xquare.app.xquareinfra.adapter.out.external.gocd.client.GocdClient
import xquare.app.xquareinfra.adapter.out.external.gocd.client.dto.request.RunSelectedJobRequest
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.SaveDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.model.ContainerStatus
import xquare.app.xquareinfra.domain.container.model.Language
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.env.cloudflare.CloudflareProperties
import xquare.app.xquareinfra.infrastructure.env.kubernetes.XquareProperties
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.integration.kubernetes.KubernetesOperationService
import xquare.app.xquareinfra.infrastructure.integration.vault.VaultService
import java.time.LocalDateTime
import java.util.*

@Transactional
@Service
class ContainerService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val cloudflareClient: CloudflareClient,
    private val cloudflareProperties: CloudflareProperties,
    private val xquareProperties: XquareProperties,
    private val saveContainerPort: SaveContainerPort,
    private val vaultService: VaultService,
    private val kubernetesOperationService: KubernetesOperationService,
    private val gocdClient: GocdClient,
    private val githubClient: GithubClient,
    private val githubProperties: GithubProperties,
    private val saveDeployPort: SaveDeployPort,
    private val findTeamPort: FindTeamPort
): ContainerUseCase {
    override fun getContainerByDeploy(deployId: UUID, user: User): List<SimpleContainerResponse> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        if(!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        return containers.map {
            SimpleContainerResponse(
                containerName = deploy.deployName,
                containerEnvironment = it.containerEnvironment,
                containerStatus = ContainerStatus.RUNNING, // TODO:: 실제 상태 조회 로직 작성
                repository = "${deploy.organization}/${deploy.repository}",
                domain = ContainerUtil.generateDomain(it, deploy),
                lastDeploy = it.lastDeploy
            )
        }
    }

    override fun syncContainerDomain(
        deployName: String,
        containerEnvironment: ContainerEnvironment,
        domain: String
    ) {
        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment) ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        saveContainerPort.save(container.updateDomain(domain))

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

    override fun syncContainer(syncContainerRequest: SyncContainerRequest) {
        val deploy = findDeployPort.findByDeployName(syncContainerRequest.deployName)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, syncContainerRequest.containerEnvironment)
        var containerId: UUID? = null
        if(container != null) {
            containerId = container.id
        }

        saveContainerPort.save(
            syncContainerRequest.run {
                Container(
                    id = containerId,
                    deployId = deploy.id!!,
                    containerEnvironment = containerEnvironment,
                    lastDeploy = LocalDateTime.now(),
                    subDomain = syncContainerRequest.subDomain,
                    environmentVariable = container?.environmentVariable ?: mapOf()
                )
            }
        )
    }

    override fun updateContainerWebhook(
        updateContainerWebhookRequest: UpdateContainerWebhookRequest,
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        saveContainerPort.save(container.updateWebhookUrl(
            webhookUrl = updateContainerWebhookRequest.webhookUrl,
            webhookType = updateContainerWebhookRequest.webhookType
        ))
    }

    override fun getEnvironmentVariable(deployId: UUID, environment: ContainerEnvironment, user: User): Map<String, String> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if(!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        return container.environmentVariable
    }

    override fun updateEnvironmentVariable(
        deployId: UUID,
        environment: ContainerEnvironment,
        environmentVariable: Map<String, String>,
        user: User
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        if(!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        saveContainerPort.save(container.updateEnvironmentVariable(environmentVariable))

        val path = vaultService.getPath(deploy, container)
        vaultService.addSecret(environmentVariable, path)

        val namespace = "${team.teamNameEn}-${container.containerEnvironment.name}"
        kubernetesOperationService.deleteSecret(namespace, path)

        if(deploy.v2) {
            val pipelineName = "build-${deploy.deployName}-${container.containerEnvironment.name}"
            val pipelinesHistory = gocdClient.getPipelinesHistory(
                pipelineName,
                "application/vnd.go.cd.v1+json"
            )

            if(pipelinesHistory.statusCode.is4xxClientError) {
                return
            }

            pipelinesHistory.body?.pipelines?.get(0)?.let {
                gocdClient.runSelectedJob(
                    pipelineName,
                    it.counter,
                    "deploy",
                    "application/vnd.go.cd.v3+json",
                    RunSelectedJobRequest(jobs = listOf("deploy"))
                )
            }
        }
    }

    override fun setContainerConfig(deployId: UUID, setContainerConfigRequest: SetContainerConfigRequest) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        setContainerConfigRequest.stag?.let {
            processContainerConfig(deploy, it, ContainerEnvironment.stag, setContainerConfigRequest.language, setContainerConfigRequest.criticalService)
        }

        setContainerConfigRequest.prod?.let {
            processContainerConfig(deploy, it, ContainerEnvironment.prod, setContainerConfigRequest.language, setContainerConfigRequest.criticalService)
        }
    }

    private fun processContainerConfig(deploy: Deploy, config: ContainerConfigDetails, environment: ContainerEnvironment, language: Language, criticalService: Boolean) {
        var container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
        var containerId: UUID? = null
        if (container != null) {
            containerId = container.id
        }

        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        container = saveContainerPort.save(
            Container(
                id = containerId,
                deployId = deploy.id!!,
                containerEnvironment = environment,
                lastDeploy = LocalDateTime.now(),
                subDomain = config.domain,
                environmentVariable = container?.environmentVariable ?: mapOf(),
                githubBranch = config.branch,
                containerPort = config.containerPort,
            )
        )

        githubClient.dispatchWorkflow(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "write-values",
                client_payload = mapOf(
                    "club" to team.teamNameEn.lowercase(Locale.getDefault()),
                    "name" to deploy.deployName,
                    "organization" to deploy.organization,
                    "repository" to deploy.repository,
                    "branch" to container.githubBranch!!,
                    "environment" to container.containerEnvironment.name,
                    "containerPort" to container.containerPort!!,
                    "domain" to container.subDomain!!,
                    "language" to language,
                    "critical_service" to criticalService
                )
            )
        )

        saveDeployPort.saveDeploy(deploy.migrationToV2())
    }

    override fun getContainerDetails(deployId: UUID, environment: ContainerEnvironment): GetContainerDetailsResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        return GetContainerDetailsResponse(
            teamNameEn = team.teamNameEn,
            deployName = deploy.deployName,
            repository = "${deploy.organization}/${deploy.repository}",
            domain = ContainerUtil.generateDomain(container, deploy),
            lastDeploy = container.lastDeploy,
            containerStatus = ContainerStatus.RUNNING, // TODO:: 실제 상태 조회 로직 작성,
            teamNameKo = team.teamNameKo,
            containerName = ContainerUtil.getContainerName(deploy, container),
            isV2 = deploy.v2
        )
    }
}