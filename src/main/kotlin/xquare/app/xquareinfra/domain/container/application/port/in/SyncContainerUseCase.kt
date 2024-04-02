package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest

interface SyncContainerUseCase {
    fun syncContainer(syncContainerRequest: SyncContainerRequest)
}