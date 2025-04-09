package xquare.app.xquareinfra.adapter.`in`.user.dto.response

import java.util.UUID

data class UserSearchResponse(
    val numberAndName: String,
    val userId: UUID
)