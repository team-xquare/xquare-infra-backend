package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.SyncContainerUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

@RestController
class ContainerWebAdapter(
    private val syncContainerUseCase: SyncContainerUseCase
) {
    @PostMapping("/sync-container")
    fun syncContainer(
        @RequestParam("deploy_name", required = true)
        deployName: String,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("domain", required = false)
        domain: String
    ) = syncContainerUseCase.syncContainer(SyncContainerRequest(deployName, environment, domain))
}