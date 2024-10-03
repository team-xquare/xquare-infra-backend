package xquare.app.xquareinfra.adapter.out.external.github

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.GithubClient
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest
import xquare.app.xquareinfra.adapter.out.external.github.env.GithubProperties
import xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.application.team.port.out.SyncTeamPort

@Component
class GithubAdapter(
    private val githubProperties: GithubProperties,
    private val githubClient: GithubClient,
    private val objectMapper: ObjectMapper,
): CreateDockerfilePort, SyncTeamPort {
    override fun <T : DockerfileRequest> createDockerfile(
        deployName: String,
        environment: ContainerEnvironment,
        dockerfileRequest: T
    ) {
        githubClient.dispatchWorkflowGitops(
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

    override fun syncTeam(teamName: String) {
        githubClient.dispatchWorkflowK8sResources(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "add_club",
                client_payload = mapOf(
                    "club_name" to teamName
                )
            )
        )
    }
}