package xquare.app.xquareinfra.adapter.out.external.github

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.`in`.github.dto.response.TokenExchangeResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.V2PipelineGithubClient
import xquare.app.xquareinfra.adapter.out.external.github.client.GithubOAuthClient
import xquare.app.xquareinfra.adapter.out.external.github.client.V3PipelineGithubClient
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.LoginAccessTokenRequest
import xquare.app.xquareinfra.adapter.out.external.github.env.GithubProperties
import xquare.app.xquareinfra.application.container.port.out.CreateDockerfilePort
import xquare.app.xquareinfra.application.container.port.out.WriteValuesPort
import xquare.app.xquareinfra.application.deploy.port.out.DeleteDeployPort
import xquare.app.xquareinfra.application.github.port.out.GithubOAuthPort
import xquare.app.xquareinfra.application.team.port.out.SyncTeamPort
import xquare.app.xquareinfra.domain.container.model.Language

@Component
class GithubAdapter(
    private val githubProperties: GithubProperties,
    private val v2PipelineGithubClient: V2PipelineGithubClient,
    private val objectMapper: ObjectMapper,
    private val githubOAuthClient: GithubOAuthClient,
    private val v3PipelineGithubClient: V3PipelineGithubClient
): CreateDockerfilePort, SyncTeamPort, WriteValuesPort, DeleteDeployPort, GithubOAuthPort {
    override fun <T : DockerfileRequest> createDockerfile(
        deployName: String,
        environment: ContainerEnvironment,
        dockerfileRequest: T
    ) {
        v2PipelineGithubClient.dispatchWorkflowGitops(
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
        v2PipelineGithubClient.dispatchWorkflowK8sResources(
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

    override fun v1WriteValues(
        club: String,
        name: String,
        organization: String,
        repository: String,
        branch: String,
        environment: String,
        containerPort: Int,
        domain: String,
        language: Language,
        criticalService: Boolean
    ) {
        v2PipelineGithubClient.dispatchWorkflowGitops(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "write-values",
                client_payload = mapOf(
                    "club" to club,
                    "name" to name,
                    "organization" to organization,
                    "repository" to repository,
                    "branch" to branch,
                    "environment" to environment,
                    "containerPort" to containerPort,
                    "domain" to domain,
                    "language" to language,
                    "criticalService" to criticalService
                )
            )
        )
    }

    override fun v2WriteValues(
        club: String,
        name: String,
        organization: String,
        repository: String,
        branch: String,
        environment: String,
        containerPort: Int,
        domain: String,
        language: Language,
        criticalService: Boolean,
        buildConfig: String,
        appInstallId: Int,
    ) {
        v3PipelineGithubClient.dispatchWorkflowGitops(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "config-api",
                client_payload = mapOf(
                    "path" to "$club/$name/$environment",
                    "action" to "apply",
                    "spec" to mapOf(
                        "organization" to organization,
                        "repository" to repository,
                        "branch" to branch,
                        "containerPort" to containerPort,
                        "domain" to domain,
                        "language" to language,
                        "criticalService" to criticalService,
                        "githubInstallationID" to appInstallId,
                        "buildConfig" to buildConfig,
                        "port" to containerPort
                    )
                )
            )
        )
    }

    override fun deleteDeploy(
        club: String,
        serviceName: String
    ) {
        v2PipelineGithubClient.dispatchWorkflowGitops(
            authorization = "Bearer ${githubProperties.token}",
            accept = "application/vnd.github.v3+json",
            request = DispatchEventRequest(
                event_type = "delete-service",
                client_payload = mapOf(
                    "club" to club,
                    "service_name" to serviceName
                )
            )
        )
    }

    override fun exchangeToken(code: String): TokenExchangeResponse {
        return githubOAuthClient.loginAccessToken(
            LoginAccessTokenRequest(
                code = code,
                client_secret = githubProperties.clientSecret,
                client_id = githubProperties.clientId,
                redirect_uri = githubProperties.redirectUri
            )
        ).run {
            TokenExchangeResponse(
                accessToken = accessToken,
                tokenType = tokenType,
                scope = scope
            )
        }
    }
}