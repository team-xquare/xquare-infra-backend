package xquare.app.xquareinfra.infrastructure.persistence.container

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.model.WebhookInfo
import xquare.app.xquareinfra.domain.container.model.WebhookType
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "tbl_container")
class ContainerJpaEntity(
    id: UUID?,
    deployJpaEntity: DeployJpaEntity,
    containerEnvironment: ContainerEnvironment,
    lastDeploy: LocalDateTime,
    subDomain: String?,
    environmentVariable: Map<String, String>,
    githubBranch: String? = null,
    containerPort: Int? = null,
    webhookInfo: WebhookInfo? = null,
) : BaseUUIDEntity(id) {
    @OneToOne
    @JoinColumn(name = "deploy_id")
    var deployJpaEntity: DeployJpaEntity = deployJpaEntity
        protected set

    @Column(name = "container_environment", nullable = false)
    @Enumerated(EnumType.STRING)
    var containerEnvironment: ContainerEnvironment = containerEnvironment
        protected set

    @Column(name = "last_deploy", nullable = false)
    var lastDeploy: LocalDateTime = lastDeploy
        protected set

    @Column(name = "sub_domain", nullable = true)
    var subDomain: String? = subDomain
        protected set

    @Column(name = "github_branch", nullable = true)
    var githubBranch: String? = githubBranch

    @Column(name = "container_port", nullable = true)
    var containerPort: Int? = containerPort

    @ElementCollection
    @MapKeyColumn(name="variable_key", length=100)
    @Column(name = "environment_variable", columnDefinition = "TEXT")
    var environmentVariable: Map<String, String> = environmentVariable
        protected set

    @Embedded
    var webhookInfo: WebhookInfo? = webhookInfo
        protected set

    fun updateEnvironmentVariable(environmentVariable: Map<String, String>) {
        this.environmentVariable = environmentVariable
    }

    fun updateGithubBranch(githubBranch: String) {
        this.githubBranch = githubBranch
    }

    fun updateContainerPort(containerPort: Int) {
        this.containerPort = containerPort
    }

    fun updateDomain(domain: String) {
        this.subDomain = domain
    }

    fun updateWebhookUrl(webhookUrl: String, webhookType: WebhookType) {
        this.webhookInfo = WebhookInfo(webhookType, webhookUrl)
    }
}