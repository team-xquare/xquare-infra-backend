package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.request.ContainerConfigDetails
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.application.container.port.`in`.SetContainerConfigUseCase
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.domain.Language
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.external.github.client.GithubClient
import xquare.app.xquareinfra.infrastructure.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.infrastructure.external.github.env.GithubProperties
import java.time.LocalDateTime
import java.util.*

@Transactional
@Service
class SetContainerConfigService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val githubClient: GithubClient,
    private val githubProperties: GithubProperties,
    private val saveContainerPort: xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
) : xquare.app.xquareinfra.application.container.port.`in`.SetContainerConfigUseCase {
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

        container = saveContainerPort.save(
            Container(
                id = containerId,
                deploy = deploy,
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
                    "club" to deploy.team.teamNameEn.lowercase(Locale.getDefault()),
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

        deploy.migrationToV2()
    }
}