package xquare.app.xquareinfra.domain.deploy.adapter.dto.response

import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus

data class SimpleDeployResponse(
    val deployName: String,
    val repository: String,
    val deployStatus: DeployStatus
)
