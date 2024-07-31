package xquare.app.xquareinfra.domain.deploy.adapter.dto.response

import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus

data class DeployDetailsResponse(
    val deployName: String,
    val teamNameEn: String,
    val teamNameKo: String,
    val oneLineDescription: String,
    val repository: String,
    val projectRootDir: String,
    val deployStatus: DeployStatus,
    val githubFullUrl: String,
    val isV2: Boolean
)
