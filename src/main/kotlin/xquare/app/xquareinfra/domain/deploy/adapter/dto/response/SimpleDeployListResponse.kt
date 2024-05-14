package xquare.app.xquareinfra.domain.deploy.adapter.dto.response

data class SimpleDeployListResponse(
    val teamNameKo: String,
    val deployList: List<SimpleDeployResponse>
)
