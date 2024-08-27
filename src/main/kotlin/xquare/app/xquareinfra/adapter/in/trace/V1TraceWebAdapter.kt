package xquare.app.xquareinfra.adapter.`in`.trace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.application.trace.port.`in`.SpanUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

@RequestMapping("/trace")
@RestController
class V1TraceWebAdapter(
    private val spanUseCase: SpanUseCase
) {
    @GetMapping("/list")
    fun getRootSpan(
        @RequestParam("deployId") deployId: UUID,
        @RequestParam("environment") environment: ContainerEnvironment,
        @RequestParam("timeRange") timeRange: Long
    ): GetRootSpanListResponse {
        return spanUseCase.getRootSpanByDeployIdAndEnvironment(
            deployId = deployId,
            environment = environment,
            timeRangeMinute = timeRange
        )
    }
}