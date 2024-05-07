package xquare.app.xquareinfra.domain.user.adapter.dto.response

import java.util.UUID

data class UserSearchResponse(
    val numberAndName: String,
    val userId: UUID
)