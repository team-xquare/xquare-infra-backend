package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SetContainerConfigRequest
import java.util.*

interface SetContainerConfigUseCase {
    fun setContainerConfig(
        deployId: UUID,
        setContainerConfigRequest: SetContainerConfigRequest
    )
}