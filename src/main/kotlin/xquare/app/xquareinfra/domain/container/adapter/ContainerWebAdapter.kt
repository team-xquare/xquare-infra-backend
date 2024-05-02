package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetEnvironmentVariableUseCase
import xquare.app.xquareinfra.domain.container.application.port.`in`.SyncContainerUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

@RequestMapping("/container")
@RestController
class ContainerWebAdapter(
    private val syncContainerUseCase: SyncContainerUseCase,
    private val getEnvironmentVariableUseCase: GetEnvironmentVariableUseCase
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
}