package xquare.app.xquareinfra.adapter.out.external.gocd.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xquare.app.xquareinfra.infrastructure.external.gocd.client.dto.response.GetPipelinesHistoryResponse
import xquare.app.xquareinfra.adapter.out.external.gocd.client.dto.request.RunSelectedJobRequest
import xquare.app.xquareinfra.adapter.out.external.feign.config.FeignConfig
import xquare.app.xquareinfra.infrastructure.external.gocd.config.GocdAuthConfig

@FeignClient(
    name = "gocd-client",
    url = "\${url.gocd}",
    configuration = [FeignConfig::class,GocdAuthConfig::class],
)
interface GocdClient {
    @GetMapping("/api/pipelines/{pipeline_name}/history")
    fun getPipelinesHistory(
        @PathVariable("pipeline_name") pipelineName: String,
        @RequestHeader("Accept") accept: String
    ): ResponseEntity<GetPipelinesHistoryResponse>

    @GetMapping("/files/{pipelineName}/{pipelineCounter}/{stage}/1/{stage}/cruise-output/console.log")
    fun getStageLog(
        @PathVariable("pipelineName") pipelineName: String,
        @PathVariable("pipelineCounter") pipelineCounter: Int,
        @PathVariable("stage") stage: String,
        @RequestHeader("Accept") accept: String
    ): String

    @PostMapping("/api/stages/{pipelineName}/{pipelineCounter}/{stage}/1/run-selected-jobs")
    fun runSelectedJob(
        @PathVariable("pipelineName") pipelineName: String,
        @PathVariable("pipelineCounter") pipelineCounter: Int,
        @PathVariable("stage") stage: String,
        @RequestHeader("Accept") accept: String,
        @RequestBody runSelectedJobRequest: RunSelectedJobRequest
    )

    @PostMapping("/api/pipelines/{pipelineName}/schedule")
    fun schedulePipeline(
        @PathVariable("pipelineName") pipelineName: String,
        @RequestHeader("Accept") accept: String,
        @RequestHeader("X-GoCD-Confirm") xGocdConfirm: Boolean = true,
    )
}
