package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.*
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

@RequestMapping("/container")
@RestController
class ContainerWebAdapter(
    private val syncContainerUseCase: SyncContainerUseCase,
    private val getEnvironmentVariableUseCase: GetEnvironmentVariableUseCase,
    private val updateEnvironmentVariableUseCase: UpdateEnvironmentVariableUseCase,
    private val getContainerByDeployIdUseCase: GetContainerByDeployIdUseCase,
    private val getContainerCpuUsageUseCase: GetContainerCpuUsageUseCase,
    private val getContainerMemoryUsageUseCase: GetContainerMemoryUsageUseCase
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
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): Map<String, String> {
        return getEnvironmentVariableUseCase.getEnvironmentVariable(deployName, environment)
    }

    @PatchMapping("/environment-variable")
    fun updateEnvironmentVariable(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestBody
        environmentVariable: Map<String, String>
    ) {
        updateEnvironmentVariableUseCase.updateEnvironmentVariable(deployName, environment, environmentVariable)
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
    ): MutableMap<String, Map<String, String>> =
        getContainerCpuUsageUseCase.getContainerCpuUsage(deployId, environment)

    @GetMapping("/memory")
    fun getContainerMemoryUsage(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): MutableMap<String, Map<String, String>> =
        getContainerMemoryUsageUseCase.getContainerMemoryUsageUseCase(deployId, environment)
}