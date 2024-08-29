package xquare.app.xquareinfra.application.trace.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.RootSpan
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.util.*

@Service
class TraceService(
    private val findDeployPort: FindDeployPort,
    private val findTracePort: FindTracePort
) : TraceUseCase {
    override fun getRootSpanByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRangeMinute: Long
    ): GetRootSpanListResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)

        val timeRangeInNanos = TimeUtil.getTimeRangeInNanos(timeRangeMinute)

        val traceList = findTracePort.findTracesByServiceNameInTimeRange(
            serviceName = serviceName,
            startTimeNano = timeRangeInNanos.past,
            endTimeNano = timeRangeInNanos.now
        )

        val rootSpanList = traceList.mapNotNull {
            it.getRootSpan()?.let { rootSpan ->
                RootSpan(
                    date = TimeUtil.unixNanoToKoreanTime(rootSpan.startTimeUnixNano),
                    resource = rootSpan.name,
                    durationMs = TimeUtil.unixNanoToMilliseconds(rootSpan.endTimeUnixNano - rootSpan.startTimeUnixNano),
                    method = rootSpan.getAttributeValue("http.method"),
                    statusCode = rootSpan.getAttributeValue("http.status_code")?.toLong()
                )
            }
        }

        return GetRootSpanListResponse(rootSpanList)
    }
}