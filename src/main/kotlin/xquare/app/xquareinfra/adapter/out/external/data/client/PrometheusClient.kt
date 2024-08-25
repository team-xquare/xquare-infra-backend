package xquare.app.xquareinfra.adapter.out.external.data.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.PrometheusDataQueryResponse
import xquare.app.xquareinfra.adapter.out.external.feign.config.FeignConfig

@FeignClient(
    name = "prometheus-client",
    url = "\${url.prometheus}",
    configuration = [FeignConfig::class]
)
interface PrometheusClient {
    @GetMapping("/api/v1/query_range")
    fun query(
        @RequestParam("query") query: String,
        @RequestParam("start") start: String,
        @RequestParam("end") end: String,
        @RequestParam("step") step: String,
    ): PrometheusDataQueryResponse
}