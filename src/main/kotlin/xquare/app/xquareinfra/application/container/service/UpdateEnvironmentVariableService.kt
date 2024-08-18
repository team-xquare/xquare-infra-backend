package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.container.port.`in`.UpdateEnvironmentVariableUseCase
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.external.gocd.client.GocdClient
import xquare.app.xquareinfra.infrastructure.external.gocd.client.dto.request.RunSelectedJobRequest
import xquare.app.xquareinfra.infrastructure.kubernetes.KubernetesClientUtil
import xquare.app.xquareinfra.infrastructure.vault.VaultUtil
import java.util.UUID

@Transactional
@Service
class UpdateEnvironmentVariableService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val vaultUtil: VaultUtil,
    private val kubernetesClientUtil: KubernetesClientUtil,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val gocdClient: GocdClient
): xquare.app.xquareinfra.application.container.port.`in`.UpdateEnvironmentVariableUseCase {
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

        if(deploy.isV2) {
            val pipelineName = "build-${deploy.deployName}-${container.containerEnvironment.name}"
            val pipelinesHistory = gocdClient.getPipelinesHistory(
                pipelineName,
                "application/vnd.go.cd.v1+json"
            )

            if(pipelinesHistory.statusCode.is4xxClientError) {
                return
            }

            pipelinesHistory.body?.pipelines?.get(0)?.let {
                gocdClient.runSelectedJob(
                    pipelineName,
                    it.counter,
                    "deploy",
                    "application/vnd.go.cd.v3+json",
                    RunSelectedJobRequest(jobs = listOf("deploy"))
                )
            }
        }
    }
}