package xquare.app.xquareinfra.infrastructure.feign.client.dsm.dto

data class GetDsmUserInfoResponse(
    val id: String,
    val accountId: String,
    val password: String,
    val name: String,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val userRole: DsmUserRole,
    val birthDay: String
)
