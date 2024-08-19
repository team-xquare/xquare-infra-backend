package xquare.app.xquareinfra.adapter.`in`.deploy.dto.request

import xquare.app.xquareinfra.domain.deploy.model.DeployType

data class CreateDeployRequest(
    val deployName: String,
    val organization: String,
    val repository: String,
    val projectRootDir: String,
    val oneLineDescription: String,
    val deployType: DeployType,
    val useRedis: Boolean,
    val useMysql: Boolean
)
