package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceListResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface TraceUseCase {
<<<<<<< HEAD
    fun getRootSpanByDeployIdAndEnvironment(deployId: UUID, environment: ContainerEnvironment, timeRangeMinute: Long): GetTraceListResponse
=======
    fun getRootSpanByDeployIdAndEnvironment(deployId: UUID, environment: ContainerEnvironment, timeRangeMinute: Long): GetRootSpanListResponse
>>>>>>> trace
}