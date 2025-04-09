package xquare.app.xquareinfra.domain.container.model


import java.time.LocalDateTime
import java.util.*

data class Container(
    val id: UUID?,
    val deployId: UUID,
    val containerEnvironment: ContainerEnvironment,
    val lastDeploy: LocalDateTime,
    val subDomain: String?,
    val environmentVariable: Map<String, String>,
    val githubBranch: String? = null,
    val containerPort: Int? = null,
    val webhookInfo: WebhookInfo? = null
) {
    fun updateEnvironmentVariable(environmentVariable: Map<String, String>) =
        copy(environmentVariable = environmentVariable)

    fun updateGithubBranch(githubBranch: String) =
        copy(githubBranch = githubBranch)

    fun updateContainerPort(containerPort: Int) =
        copy(containerPort = containerPort)

    fun updateDomain(domain: String) =
        copy(subDomain = domain)

    fun updateWebhookUrl(webhookUrl: String, webhookType: WebhookType) =
        copy(webhookInfo = WebhookInfo(webhookType, webhookUrl))
}