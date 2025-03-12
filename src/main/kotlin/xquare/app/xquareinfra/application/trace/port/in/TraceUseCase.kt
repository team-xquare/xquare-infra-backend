package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface TraceUseCase {
    fun getServiceEmbedDashboard(deployId: UUID, environment: ContainerEnvironment): String
}