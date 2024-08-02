package xquare.app.xquareinfra.domain.container.application.port.`in`

import java.util.UUID

interface GetStageLogUseCase {
    fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String
}