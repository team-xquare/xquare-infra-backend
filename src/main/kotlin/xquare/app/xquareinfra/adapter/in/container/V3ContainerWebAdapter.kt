package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.CreateV3ApplicationRequest
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v3/container")
@RestController
class V3ContainerWebAdapter(
    private val containerUseCase: ContainerUseCase
) {
    @PostMapping
    fun createContainer(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createV3ApplicationRequest: CreateV3ApplicationRequest
    ) {
        containerUseCase.createV3Container(
            deployId = deployId,
            containerEnvironment = containerEnvironment,
            createV3ApplicationRequest = createV3ApplicationRequest
        )
    }
}