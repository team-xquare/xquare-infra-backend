package xquare.app.xquareinfra.adapter.out.external.deploy.client

import xquare.app.xquareinfra.adapter.out.external.deploy.client.dto.request.FeignCreateDeployRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import xquare.app.xquareinfra.infrastructure.external.feign.client.deploy.dto.response.DeployResponse
import xquare.app.xquareinfra.infrastructure.external.feign.config.FeignConfig

@FeignClient(
    name = "deploy-client",
    url = "\${url.deploy}",
    configuration = [FeignConfig::class]
)
interface DeployClient {
    @PostMapping("/project")
    fun createDeploy(@RequestBody feignCreateDeployRequest: FeignCreateDeployRequest): feign.Response

    @GetMapping("/project/all")
    fun getAllDeploy(@RequestParam("email") email: String): List<DeployResponse>
}