package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface SetContainerConfigUseCase {
    fun setContainerConfig(
        deployId: UUID,
        setContainerConfigRequest: SetContainerConfigRequest
    )
}