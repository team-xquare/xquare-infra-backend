package xquare.app.xquareinfra.infrastructure.feign.client.deploy

import xquare.app.xquareinfra.infrastructure.feign.client.deploy.dto.FeignCreateDeployRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xquare.app.xquareinfra.infrastructure.feign.config.FeignConfig

@FeignClient(
    name = "deploy-client",
    url = "\${url.deploy}",
    configuration = [FeignConfig::class]
)
interface DeployClient {
    @PostMapping("/project")
    fun createDeploy(@RequestBody feignCreateDeployRequest: FeignCreateDeployRequest): feign.Response
}