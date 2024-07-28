package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import java.util.UUID

interface DeployMigrationToV2PipelineUseCase {
    fun migrationDeploy(deployId: UUID)
}