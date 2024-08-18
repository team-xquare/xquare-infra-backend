package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest

interface SyncContainerUseCase {
    fun syncContainer(syncContainerRequest: SyncContainerRequest)
}