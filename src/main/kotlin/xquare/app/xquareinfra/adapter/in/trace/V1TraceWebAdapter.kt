package xquare.app.xquareinfra.adapter.`in`.trace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceListResponse
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
        @RequestParam("timeRange") timeRange: Long
    ): GetTraceListResponse {
        return traceUseCase.getRootSpanByDeployIdAndEnvironment(
            deployId = deployId,
            environment = environment,
            timeRangeMinute = timeRange
        )
    }
}