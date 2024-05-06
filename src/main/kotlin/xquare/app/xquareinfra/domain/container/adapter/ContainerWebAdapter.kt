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
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerLogUseCase
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetEnvironmentVariableUseCase
import xquare.app.xquareinfra.domain.container.application.port.`in`.SyncContainerUseCase
import xquare.app.xquareinfra.domain.container.application.port.`in`.UpdateEnvironmentVariableUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

@RequestMapping("/container")
@RestController
class ContainerWebAdapter(
    private val syncContainerUseCase: SyncContainerUseCase,
    private val getEnvironmentVariableUseCase: GetEnvironmentVariableUseCase,
    private val updateEnvironmentVariableUseCase: UpdateEnvironmentVariableUseCase,
    private val getContainerLogUseCase: GetContainerLogUseCase
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
}