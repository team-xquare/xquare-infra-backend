package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.out.external.gocd.client.GocdClient

@Service
class GetStageLogService(
    private val gocdClient: GocdClient
): xquare.app.xquareinfra.application.container.port.`in`.GetStageLogUseCase {
    override fun getStageLog(pipelineCounter: Int, stageName: String, pipelineName: String): String {
        return gocdClient.getStageLog(pipelineName, pipelineCounter, stageName, "")
    }
}