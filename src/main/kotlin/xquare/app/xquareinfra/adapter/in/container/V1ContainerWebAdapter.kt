package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v1/container")
@RestController
class V1ContainerWebAdapter(
    private val securityPort: SecurityPort,
    private val containerUseCase: ContainerUseCase
) {
    @PostMapping("/sync")
    fun syncContainer(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("domain", required = false)
        domain: String
    ) = containerUseCase.syncContainer(SyncContainerRequest(deployName, environment, domain))

    @GetMapping("/environment-variable")
    fun getEnvironmentVariable(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, String> {
        return containerUseCase.getEnvironmentVariable(deployId, environment, securityPort.getCurrentUser())
    }

    @PatchMapping("/environment-variable")
    fun updateEnvironmentVariable(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestBody
        environmentVariable: Map<String, String>
    ) {
        containerUseCase.updateEnvironmentVariable(deployId, environment, environmentVariable, securityPort.getCurrentUser())
    }

    @GetMapping
    fun getContainerByDeployId(
        @RequestParam("deployId")
        deployId: UUID
    ): List<SimpleContainerResponse> = containerUseCase.getContainerByDeploy(deployId, securityPort.getCurrentUser())

    @GetMapping("/details")
    fun getContainerDetails(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): GetContainerDetailsResponse = containerUseCase.getContainerDetails(deployId, environment)
}