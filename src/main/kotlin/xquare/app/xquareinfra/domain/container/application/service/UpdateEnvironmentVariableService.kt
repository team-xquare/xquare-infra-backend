package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.application.port.`in`.UpdateEnvironmentVariableUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.external.client.gocd.GocdClient
import xquare.app.xquareinfra.infrastructure.external.feign.client.gocd.dto.request.RunSelectedJobRequest
import xquare.app.xquareinfra.infrastructure.kubernetes.KubernetesClientUtil
import xquare.app.xquareinfra.infrastructure.vault.VaultUtil
import java.util.UUID

@Transactional
@Service
class UpdateEnvironmentVariableService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val vaultUtil: VaultUtil,
    private val kubernetesClientUtil: KubernetesClientUtil,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val gocdClient: GocdClient
): UpdateEnvironmentVariableUseCase {
    override fun updateEnvironmentVariable(
        deployId: UUID,
        environment: ContainerEnvironment,
        environmentVariable: Map<String, String>
    ) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if(!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        container.updateEnvironmentVariable(environmentVariable)

        val path = vaultUtil.getPath(deploy, container)
        vaultUtil.addSecret(environmentVariable, path)

        val namespace = "${deploy.team.teamNameEn}-${container.containerEnvironment.name}"
        kubernetesClientUtil.deleteSecret(namespace, path)

        val pipelineName = "build-${deploy.deployName}-${container.containerEnvironment.name}"
        val accept = "application/vnd.go.cd.v1+json"
        val pipelinesHistory = gocdClient.getPipelinesHistory(
            pipelineName,
            accept
        )

        pipelinesHistory.pipelines?.get(0)?.let {
            gocdClient.runSelectedJob(
                pipelineName,
                it.counter,
                "deploy",
                accept,
                RunSelectedJobRequest(jobs = listOf("deploy"))
            )
        }
    }
}