package xquare.app.xquareinfra.adapter.`in`.deploy.dto.response

import java.util.UUID

data class CreateDeployResponse(
    val deployId: UUID,
    val teamId: UUID
)
