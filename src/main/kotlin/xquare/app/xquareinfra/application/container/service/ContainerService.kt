package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.adapter.out.external.github.client.V2PipelineGithubClient
import xquare.app.xquareinfra.adapter.out.external.github.env.GithubProperties
import xquare.app.xquareinfra.adapter.out.external.gocd.client.GocdClient
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.application.container.port.out.ContainerDnsPort
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.application.container.port.out.WriteValuesPort
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
    private val containerDnsPort: ContainerDnsPort,
    private val saveContainerPort: SaveContainerPort,
    private val writeValuesPort: WriteValuesPort,
    private val vaultService: VaultService,
    private val kubernetesOperationService: KubernetesOperationService,
    private val gocdClient: GocdClient,
    private val v2PipelineGithubClient: V2PipelineGithubClient,
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
                lastDeploy = it.lastDeploy,
                serviceFullName = ContainerUtil.getContainerName(deploy, it.containerEnvironment)
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
        val records = containerDnsPort.listDnsRecords()
        if(!records.any { it.name == domain }) {
            containerDnsPort.createGatewayDnsRecords(name = domain)
        }
    }

    override fun createV3Container(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createV3ApplicationRequest: CreateV3ApplicationRequest,
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        var container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
        var containerId: UUID? = null
        if (container != null) {
            containerId = container.id
        }

        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        saveContainerPort.save(
            Container(
                id = containerId,
                deployId = deploy.id!!,
                containerEnvironment = containerEnvironment,
                lastDeploy = LocalDateTime.now(),
                subDomain = createV3ApplicationRequest.domain,
                environmentVariable = container?.environmentVariable ?: mapOf(),
                githubBranch = createV3ApplicationRequest.branch,
                containerPort = createV3ApplicationRequest.containerPort,
            )
        )

        writeValuesPort.v2WriteValues(
            club = team.teamNameEn,
            name = deploy.deployName,
            organization = deploy.organization,
            repository = deploy.repository,
            branch = createV3ApplicationRequest.branch,
            containerPort = createV3ApplicationRequest.containerPort,
            domain = createV3ApplicationRequest.domain,
            language = createV3ApplicationRequest.language,
            criticalService = createV3ApplicationRequest.criticalService,
            buildConfig = createV3ApplicationRequest.buildConfig,
            appInstallId = deploy.appInstallId?.toInt() ?: throw XquareException.BAD_REQUEST,
            environment = containerEnvironment.toString()
        )
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

        writeValuesPort.v1WriteValues(
            club = team.teamNameEn.lowercase(Locale.getDefault()),
            name = deploy.deployName,
            organization = deploy.organization,
            repository = deploy.repository,
            branch = container.githubBranch!!,
            environment = container.containerEnvironment.name,
            containerPort = container.containerPort!!,
            domain = container.subDomain!!,
            language = language,
            criticalService = criticalService
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
            isV2 = deploy.v2,
            serviceFullName = ContainerUtil.getContainerName(deploy, container),
            deployType = deploy.deployType,
        )
    }
}
