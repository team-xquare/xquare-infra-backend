package xquare.app.xquareinfra.adapter.`in`.trace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceDetailResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.ServiceMapResponse
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

@RequestMapping("/trace")
@RestController
class V1TraceWebAdapter(
    private val traceUseCase: TraceUseCase
) {
    @GetMapping("/list")
    fun getRootSpan(
        @RequestParam("deployId") deployId: UUID,
        @RequestParam("environment") environment: ContainerEnvironment,
        @RequestParam("timeRange") timeRangeSecond: Long
    ): GetSpanListResponse {
        return traceUseCase.getAllSpansByDeployIdAndEnvironment(
            deployId = deployId,
            environment = environment,
            timeRangeSeconds = timeRangeSecond
        )
    }

    @GetMapping("/{traceId}")
    fun getTraceDetail(@PathVariable traceId: String): GetTraceDetailResponse {
        return traceUseCase.getTraceDetail(traceId)
    }

    @GetMapping("/service-map")
    fun getServiceMap(
        @RequestParam("teamId") teamId: UUID,
        @RequestParam("startTimeNano") startTimeNano: Long,
        @RequestParam("endTimeNano") endTimeNano: Long,
    ): ServiceMapResponse = traceUseCase.getServiceMap(teamId, startTimeNano, endTimeNano)
}