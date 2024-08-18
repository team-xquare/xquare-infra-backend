package xquare.app.xquareinfra.application.deploy.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.deploy.port.`in`.DeployMigrationToV2PipelineUseCase
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.vault.VaultUtil
import java.util.*

@Transactional
@Service
class DeployMigrationToV2PipelineService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val vaultUtil: VaultUtil
) : DeployMigrationToV2PipelineUseCase {
    override fun migrationDeploy(deployId: UUID) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        deploy.migrationToV2()

        containers.map {
            val path = vaultUtil.getPath(deploy, it)
            println(path)
            vaultUtil.addSecret(it.environmentVariable, vaultUtil.getPath(deploy, it))
        }
    }
}