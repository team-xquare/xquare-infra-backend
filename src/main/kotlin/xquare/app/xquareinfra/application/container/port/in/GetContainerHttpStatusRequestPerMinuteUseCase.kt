package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface GetContainerHttpStatusRequestPerMinuteUseCase {
    fun getContainerHttpStatusRequestPerMinute(deployId: UUID, environment: ContainerEnvironment, timeRange: Int, statusCode: Int): Map<String, Map<String, String>>
}