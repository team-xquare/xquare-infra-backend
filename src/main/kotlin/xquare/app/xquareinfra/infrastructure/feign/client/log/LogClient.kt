package xquare.app.xquareinfra.infrastructure.feign.client.log

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xquare.app.xquareinfra.infrastructure.feign.client.log.dto.GetLogRequest
import xquare.app.xquareinfra.infrastructure.feign.client.log.dto.LogResponse
import xquare.app.xquareinfra.infrastructure.feign.config.FeignConfig

@FeignClient(
    name = "tsdata-client",
    url = "\${url.log}",
    configuration = [FeignConfig::class]
)
interface LogClient {
    @PostMapping("/api/ds/query")
    fun getLogs(@RequestBody getLogRequest: GetLogRequest): LogResponse
}