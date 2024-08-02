package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetStageLogUseCase
import xquare.app.xquareinfra.infrastructure.external.client.gocd.GocdClient

@Service
class GetStageLogService(
    private val gocdClient: GocdClient
): GetStageLogUseCase {
    override fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String {
        return gocdClient.getStageLog(pipelineName, pipelineCounter, stageName, "")
    }
}