package xquare.app.xquareinfra.adapter.`in`.trace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.application.trace.service.TraceService
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RestController
@RequestMapping("/v2/trace")
class V2TraceWebAdapter(
    private val traceUseCase: TraceUseCase
) {
    @GetMapping("/shared-dashboard")
    fun getSharedDashboard(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): String {
        return traceUseCase.getServiceEmbedDashboard(deployId, environment)
    }
}