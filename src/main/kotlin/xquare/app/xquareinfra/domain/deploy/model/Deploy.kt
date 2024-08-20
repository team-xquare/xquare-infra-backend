package xquare.app.xquareinfra.domain.deploy.model

import xquare.app.xquareinfra.domain.team.model.Team
import java.util.*

data class Deploy(
    val id: UUID?,
    val deployName: String,
    val organization: String,
    val repository: String,
    val projectRootDir: String,
    val oneLineDescription: String,
    val team: Team,
    val secretKey: String?,
    val deployStatus: DeployStatus,
    val deployType: DeployType,
    val useMysql: Boolean,
    val useRedis: Boolean,
    val isV2: Boolean
) {
    fun updateSecret(secretKey: String) = copy(secretKey = secretKey)

    fun approveDeploy() = copy(deployStatus = DeployStatus.AVAILABLE)

    fun migrationToV2() = copy(isV2 = true)
}