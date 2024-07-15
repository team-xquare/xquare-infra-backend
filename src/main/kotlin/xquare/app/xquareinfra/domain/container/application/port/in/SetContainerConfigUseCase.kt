package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import java.util.*

interface SetContainerConfigUseCase {
    fun setContainerConfig(
        deployId: UUID,
        setContainerConfigRequest: SetContainerConfigRequest
    )
}