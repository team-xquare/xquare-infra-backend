package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface SpanUseCase {
    fun getRootSpanByDeployIdAndEnvironment(deployId: UUID, environment: ContainerEnvironment, timeRangeMinute: Long): GetRootSpanListResponse
}