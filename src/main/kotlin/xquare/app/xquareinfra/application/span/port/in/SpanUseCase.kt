package xquare.app.xquareinfra.application.span.port.`in`

import xquare.app.xquareinfra.adapter.`in`.span.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface SpanUseCase {
    fun getRootSpanByDeployIdAndEnvironment(deployId: UUID, environment: ContainerEnvironment, timeRangeMinute: Long): GetRootSpanListResponse
}