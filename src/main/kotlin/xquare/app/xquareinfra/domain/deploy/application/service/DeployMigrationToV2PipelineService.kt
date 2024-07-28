package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.DeployMigrationToV2PipelineUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.vault.VaultUtil
import java.util.*

@Transactional
@Service
class DeployMigrationToV2PipelineService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val vaultUtil: VaultUtil
) : DeployMigrationToV2PipelineUseCase{
    override fun migrationDeploy(deployId: UUID) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        containers.map {
            vaultUtil.addSecret(it.environmentVariable, vaultUtil.getPath(deploy, it))
        }

        deploy.migrationToV2()
    }
}