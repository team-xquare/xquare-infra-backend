package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.SetContainerConfigUseCase
import java.util.*

@RequestMapping("/v2/container")
@RestController
class V2ContainerWebAdapter(
    private val setContainerConfigUseCase: SetContainerConfigUseCase
) {
    @PutMapping("/config")
    fun setContainerConfig(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestBody
        setContainerConfigRequest: SetContainerConfigRequest
    ) = setContainerConfigUseCase.setContainerConfig(deployId, setContainerConfigRequest)
}