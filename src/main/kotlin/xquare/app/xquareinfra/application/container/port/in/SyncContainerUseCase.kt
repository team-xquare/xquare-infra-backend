package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest

interface SyncContainerUseCase {
    fun syncContainer(syncContainerRequest: SyncContainerRequest)
}