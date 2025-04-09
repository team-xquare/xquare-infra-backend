package xquare.app.xquareinfra.adapter.`in`.deploy.dto.response

data class SimpleDeployListResponse(
    val teamNameKo: String,
    val deployList: List<SimpleDeployResponse>
)
