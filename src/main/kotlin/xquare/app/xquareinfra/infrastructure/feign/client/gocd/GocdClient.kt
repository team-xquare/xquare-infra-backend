package xquare.app.xquareinfra.infrastructure.feign.client.gocd

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import xquare.app.xquareinfra.infrastructure.feign.client.gocd.dto.response.GetPipelinesHistoryResponse
import xquare.app.xquareinfra.infrastructure.feign.config.FeignConfig

@FeignClient(
    name = "gocd-client",
    url = "\${url.gocd}",
    configuration = [FeignConfig::class]
)
interface GocdClient {
    @GetMapping("/api/pipelines/{pipeline_name}/history")
    fun getPipelinesHistory(
        @PathVariable("pipeline_name") pipelineName: String,
        @RequestHeader("Accept") accept: String
    ): GetPipelinesHistoryResponse
}
