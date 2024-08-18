package xquare.app.xquareinfra.application.deploy.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.deploy.port.`in`.DeployMigrationToV2PipelineUseCase
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.vault.VaultService
import java.util.*

@Transactional
@Service
class DeployMigrationToV2PipelineService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val vaultService: VaultService
) : DeployMigrationToV2PipelineUseCase {
    override fun migrationDeploy(deployId: UUID) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        deploy.migrationToV2()

        containers.map {
            val path = vaultService.getPath(deploy, it)
            println(path)
            vaultService.addSecret(it.environmentVariable, vaultService.getPath(deploy, it))
        }
    }
}