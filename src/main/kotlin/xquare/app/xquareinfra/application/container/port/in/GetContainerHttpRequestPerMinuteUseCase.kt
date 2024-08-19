package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface GetContainerHttpRequestPerMinuteUseCase {
    fun getContainerHttpRequestPerMinute(deployId: UUID, environment: ContainerEnvironment, timeRange: Int): Map<String, Map<String, String>>
}