package xquare.app.xquareinfra.infrastructure.external.github

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.infrastructure.external.github.client.GithubClient
import xquare.app.xquareinfra.infrastructure.external.github.client.dto.request.DockerfileRequest
import xquare.app.xquareinfra.infrastructure.external.github.env.GithubProperties

@Component
class GithubAdapter(
    private val githubProperties: GithubProperties,
    private val githubClient: GithubClient,
    private val objectMapper: ObjectMapper,
): xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort {
    override fun <T : DockerfileRequest> createDockerfile(
        deployName: String,
        environment: ContainerEnvironment,
        dockerfileRequest: T
    ) {
        githubClient.dispatchWorkflow(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "write-dockerfile",
                client_payload = mapOf(
                    "name" to deployName,
                    "environment" to environment.name,
                    "template_json" to objectMapper.writeValueAsString(dockerfileRequest),
                    "builder" to dockerfileRequest.builder
                )
            )
        )
    }
}