package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface GetContainerLatencyUseCase {
    fun getContainerLatency(deployId: UUID, environment: ContainerEnvironment, percent: Int, timeRange: Int): Map<String, Map<String, String>>
}