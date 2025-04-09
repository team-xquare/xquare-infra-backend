package xquare.app.xquareinfra.adapter.out.external.data.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.DsDataQueryResponse
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.PrometheusDataQueryResponse
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.QueryRequest
import xquare.app.xquareinfra.adapter.out.external.feign.config.FeignConfig

@FeignClient(
    name = "tsdata-client",
    url = "\${url.log}",
    configuration = [FeignConfig::class]
)
interface DataClient {
    @PostMapping("/api/ds/query")
    fun query(@RequestBody queryRequest: QueryRequest): DsDataQueryResponse
}