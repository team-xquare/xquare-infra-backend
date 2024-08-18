package xquare.app.xquareinfra.adapter.`in`.deploy.dto.response

import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus
import java.util.UUID

data class SimpleDeployResponse(
    val deployId: UUID,
    val deployName: String,
    val repository: String,
    val deployStatus: DeployStatus
)
