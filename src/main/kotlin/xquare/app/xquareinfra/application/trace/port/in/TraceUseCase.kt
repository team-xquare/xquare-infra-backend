package xquare.app.xquareinfra.application.trace.port.`in`

import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceDetailResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.ServiceMapResponse
import xquare.app.xquareinfra.adapter.out.external.datadog.client.DashboardList
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

interface TraceUseCase {
    fun getServiceEmbedDashboard(deployId: UUID, environment: ContainerEnvironment): String
}