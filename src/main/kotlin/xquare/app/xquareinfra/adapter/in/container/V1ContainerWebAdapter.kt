package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

@RequestMapping("/v1/container")
@RestController
class V1ContainerWebAdapter(
    private val syncContainerUseCase: xquare.app.xquareinfra.application.container.port.`in`.SyncContainerUseCase,
    private val getEnvironmentVariableUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetEnvironmentVariableUseCase,
    private val updateEnvironmentVariableUseCase: xquare.app.xquareinfra.application.container.port.`in`.UpdateEnvironmentVariableUseCase,
    private val getContainerByDeployIdUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerByDeployIdUseCase,
    private val getContainerCpuUsageUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerCpuUsageUseCase,
    private val getContainerMemoryUsageUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerMemoryUsageUseCase,
    private val getContainerDetailsUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerDetailsUseCase
) {
    @PostMapping("/sync")
    fun syncContainer(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("domain", required = false)
        domain: String
    ) = syncContainerUseCase.syncContainer(SyncContainerRequest(deployName, environment, domain))

    @GetMapping("/environment-variable")
    fun getEnvironmentVariable(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, String> {
        return getEnvironmentVariableUseCase.getEnvironmentVariable(deployId, environment)
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
        updateEnvironmentVariableUseCase.updateEnvironmentVariable(deployId, environment, environmentVariable)
    }

    @GetMapping
    fun getContainerByDeployId(
        @RequestParam("deployId")
        deployId: UUID
    ): List<SimpleContainerResponse> = getContainerByDeployIdUseCase.getContainerByDeploy(deployId)

    @GetMapping("/cpu")
    fun getContainerCpuUsage(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, Map<String, String>> =
        getContainerCpuUsageUseCase.getContainerCpuUsage(deployId, environment)

    @GetMapping("/memory")
    fun getContainerMemoryUsage(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, Map<String, String>> =
        getContainerMemoryUsageUseCase.getContainerMemoryUsageUseCase(deployId, environment)

    @GetMapping("/details")
    fun getContainerDetails(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): GetContainerDetailsResponse = getContainerDetailsUseCase.getContainerDetails(deployId, environment)
}