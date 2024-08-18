package xquare.app.xquareinfra.application.deploy.port.`in`

import java.util.UUID

interface DeployMigrationToV2PipelineUseCase {
    fun migrationDeploy(deployId: UUID)
}