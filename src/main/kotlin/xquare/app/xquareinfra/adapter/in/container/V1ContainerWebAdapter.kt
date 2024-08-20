package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.application.container.port.`in`.ContainerMetricUseCase
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v1/container")
@RestController
class V1ContainerWebAdapter(
    private val securityPort: SecurityPort,
    private val containerUseCase: ContainerUseCase,
    private val containerMetricUseCase: ContainerMetricUseCase,
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
        return containerUseCase.getEnvironmentVariable(deployId, environment)
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
        containerUseCase.updateEnvironmentVariable(deployId, environment, environmentVariable)
    }

    @GetMapping
    fun getContainerByDeployId(
        @RequestParam("deployId")
        deployId: UUID
    ): List<SimpleContainerResponse> = containerUseCase.getContainerByDeploy(deployId)

    @GetMapping("/cpu")
    fun getContainerCpuUsage(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, Map<String, String>> =
        containerMetricUseCase.getContainerCpuUsage(deployId, environment, securityPort.getCurrentUser())

    @GetMapping("/memory")
    fun getContainerMemoryUsage(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, Map<String, String>> =
        containerMetricUseCase.getContainerMemoryUsageUseCase(deployId, environment, securityPort.getCurrentUser())

    @GetMapping("/details")
    fun getContainerDetails(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): GetContainerDetailsResponse = containerUseCase.getContainerDetails(deployId, environment)
}