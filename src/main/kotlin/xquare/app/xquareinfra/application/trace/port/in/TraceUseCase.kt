package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceDetailResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.ServiceMapResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface TraceUseCase {
    fun getAllSpansByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRangeSeconds: Long
    ): GetSpanListResponse

    fun getTraceDetail(traceId: String): GetTraceDetailResponse

    fun getServiceMap(teamId: UUID, startTimeNano: Long, endTimeNano: Long): ServiceMapResponse
}