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
    val teamId: UUID,
    val secretKey: String?,
    val deployStatus: DeployStatus,
    val deployType: DeployType,
    val useMysql: Boolean,
    val useRedis: Boolean,
    val v2: Boolean,
    val dashboardToken: String? = null,
    val appInstallId: String? = null,
) {
    fun updateSecret(secretKey: String) = copy(secretKey = secretKey)

    fun approveDeploy() = copy(deployStatus = DeployStatus.AVAILABLE)

    fun migrationToV2() = copy(v2 = true)

    fun updateDeployToken(token: String) = copy(dashboardToken = token)

    fun updateAppInstallId(appInstallId: String) = copy(appInstallId = appInstallId)
}