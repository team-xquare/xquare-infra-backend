package xquare.app.xquareinfra.adapter.`in`.span

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.span.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.application.span.port.`in`.SpanUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

@RequestMapping("/span")
@RestController
class V1SpanWebAdapter(
    private val spanUseCase: SpanUseCase
) {
    @GetMapping("/root-span")
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