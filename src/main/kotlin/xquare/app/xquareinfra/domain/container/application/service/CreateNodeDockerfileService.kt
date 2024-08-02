package xquare.app.xquareinfra.domain.container.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.CreateNodeDockerfileUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.external.client.github.GithubClient
import xquare.app.xquareinfra.infrastructure.external.client.github.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.infrastructure.global.env.github.GithubProperties
import java.util.*

@Service
class CreateNodeDockerfileService(
    private val findDeployPort: FindDeployPort,
    private val githubClient: GithubClient,
    private val findContainerPort: FindContainerPort,
    private val githubProperties: GithubProperties,
    private val objectMapper: ObjectMapper
) : CreateNodeDockerfileUseCase {
    override fun createNodeDockerfile(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment,
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, containerEnvironment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        createNodeDockerfileRequest.port = container.containerPort

        githubClient.dispatchWorkflow(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "write-dockerfile",
                client_payload = mapOf(
                    "name" to deploy.deployName,
                    "environment" to container.containerEnvironment.name,
                    "template_json" to objectMapper.writeValueAsString(createNodeDockerfileRequest),
                    "builder" to createNodeDockerfileRequest.builder
                )
            )
        )
    }
}