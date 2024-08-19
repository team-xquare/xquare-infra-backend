package xquare.app.xquareinfra.application.container.port.`in`

import java.util.UUID

interface GetStageLogUseCase {
    fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String
}