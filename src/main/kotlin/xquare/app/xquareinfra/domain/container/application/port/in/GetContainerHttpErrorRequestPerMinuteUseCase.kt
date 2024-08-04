package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

interface GetContainerHttpErrorRequestPerMinuteUseCase {
    fun getContainerHttpErrorRequestPerMinute(deployId: UUID, environment: ContainerEnvironment): MutableMap<String, Map<String, String>>
}