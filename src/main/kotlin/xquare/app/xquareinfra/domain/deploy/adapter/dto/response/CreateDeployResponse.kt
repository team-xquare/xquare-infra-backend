package xquare.app.xquareinfra.domain.deploy.adapter.dto.response

import java.util.UUID

data class CreateDeployResponse(
    val deployId: UUID,
    val teamId: UUID
)
