package xquare.app.xquareinfra.domain.deploy.adapter.dto.response

import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus
import java.util.UUID

data class SimpleDeployResponse(
    val deployId: UUID,
    val deployName: String,
    val repository: String,
    val deployStatus: DeployStatus,
    val teamNameEn: String,
    val teamNameKo: String
)
