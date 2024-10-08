package xquare.app.xquareinfra.adapter.`in`.container.dto.response

data class GetContainerDeployHistoryResponse(
    val histories: List<DeployHistoryResponse>
)

data class DeployHistoryResponse(
    val name: String,
    val email: String,
    val scheduledDate: Long,
    val commitMessage: String,
    val stages: List<StageStatus>,
    val pipelineCounter: Int,
    val pipelineName: String
)

data class StageStatus(
    val name: String,
    val status: String
)