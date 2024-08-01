package xquare.app.xquareinfra.domain.container.adapter.dto.response

data class GetContainerDeployHistoryResponse(
    val histories: List<DeployHistoryResponse>
)

data class DeployHistoryResponse(
    val name: String,
    val email: String,
    val scheduledDate: Long,
    val commitMessage: String,
    val stages: List<StageStatus>
)

data class StageStatus(
    val name: String,
    val status: String
)