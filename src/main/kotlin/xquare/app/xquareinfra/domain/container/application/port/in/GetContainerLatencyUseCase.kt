package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface GetContainerLatencyUseCase {
    fun getContainerLatency(deployId: UUID, environment: ContainerEnvironment, percent: Int, timeRange: Int): MutableMap<String, Map<String, String>>
}