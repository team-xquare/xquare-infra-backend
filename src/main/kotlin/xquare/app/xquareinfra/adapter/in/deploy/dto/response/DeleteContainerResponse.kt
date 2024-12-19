package xquare.app.xquareinfra.adapter.`in`.deploy.dto.response

import java.util.UUID

data class DeleteContainerResponse (
    val deployId: UUID,
    val deployName: String,
)