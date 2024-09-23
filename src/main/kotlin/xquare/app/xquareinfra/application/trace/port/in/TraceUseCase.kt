package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.adapter.`in`.trace.dto.request.QueryOption
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceDetailResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface TraceUseCase {
    fun getSpansByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        queryOption: QueryOption
    ): GetRootSpanListResponse

    fun getTraceDetail(traceId: String): GetTraceDetailResponse
}