package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.request.ContainerConfigDetails
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.SetContainerConfigUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.application.port.out.SaveContainerPort
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.feign.client.github.GithubClient
import xquare.app.xquareinfra.infrastructure.feign.client.github.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.infrastructure.global.env.github.GithubProperties
import java.time.LocalDateTime
import java.util.*

@Transactional
@Service
class SetContainerConfigService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val githubClient: GithubClient,
    private val githubProperties: GithubProperties,
    private val saveContainerPort: SaveContainerPort

) : SetContainerConfigUseCase{
    override fun setContainerConfig(deployId: UUID, setContainerConfigRequest: SetContainerConfigRequest) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        setContainerConfigRequest.stag?.let {
            processContainerConfig(deploy, it, ContainerEnvironment.stag)
        }

        setContainerConfigRequest.prod?.let {
            processContainerConfig(deploy, it, ContainerEnvironment.prod)
        }
    }

    private fun processContainerConfig(deploy: Deploy, config: ContainerConfigDetails, environment: ContainerEnvironment) {
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
                    "club" to deploy.team.teamNameEn,
                    "name" to deploy.deployName,
                    "organization" to deploy.organization,
                    "repository" to deploy.repository,
                    "branch" to container.githubBranch!!,
                    "environment" to container.containerEnvironment.name,
                    "containerPort" to container.containerPort!!,
                    "domain" to container.subDomain!!
                )
            )
        )
    }
}