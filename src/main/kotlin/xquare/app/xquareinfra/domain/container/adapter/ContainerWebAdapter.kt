package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.adapter.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.*
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

@RequestMapping("/container")
@RestController
class ContainerWebAdapter(
    private val syncContainerUseCase: SyncContainerUseCase,
    private val getEnvironmentVariableUseCase: GetEnvironmentVariableUseCase,
    private val updateEnvironmentVariableUseCase: UpdateEnvironmentVariableUseCase,
    private val getContainerLogUseCase: GetContainerLogUseCase,
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

    @GetMapping("/logs")
    fun getContainerLog(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
    ): GetContainerLogResponse {
        return getContainerLogUseCase.getContainerLog(deployName, environment)
    }

    @GetMapping
    fun getContainerByDeployId(
        @RequestParam("deployId")
        deployId: UUID
    ): List<SimpleContainerResponse> = getContainerByDeployIdUseCase.getContainerByDeploy(deployId)

    @GetMapping("/cpu")
    fun getContainerCpuUsage(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): MutableMap<String, Map<String, String>> =
        getContainerCpuUsageUseCase.getContainerCpuUsage(deployName, environment)

    @GetMapping("/memory")
    fun getContainerMemoryUsage(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): MutableMap<String, Map<String, String>> =
        getContainerMemoryUsageUseCase.getContainerMemoryUsageUseCase(deployName, environment)
}