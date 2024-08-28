package xquare.app.xquareinfra.application.trace.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.GetTraceListResponse
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.Trace
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
    ): GetTraceListResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)

        val timeRangeInNanos = TimeUtil.getTimeRangeInNanos(timeRangeMinute)

<<<<<<< HEAD
        val rootSpanList = findTracePort.findTraceListByServiceNameInTimeRange(
=======
        val rootSpanList = findTracePort.findTracesByServiceNameInTimeRange(
>>>>>>> trace
            serviceName = serviceName,
            startTimeNano = timeRangeInNanos.past,
            endTimeNano = timeRangeInNanos.now
        )

<<<<<<< HEAD
        val traceDtoList = rootSpanList.map {
            val rootSpan = it.findRootSpan()
            Trace(
                dateNano = it.dateNano,
                resource = rootSpan?.name,
                durationMs = TimeUtil.unixNanoToMilliseconds(it.durationNano),
                method = rootSpan?.getAttributeValue("http.method")?.stringValue,
                statusCode = rootSpan?.getAttributeValue("http.status_code")?.intValue
            )
=======
        val rootSpanDtoList = rootSpanList.mapNotNull {
            it.getRootSpan()?.let { rootSpan ->
                RootSpan(
                    date = TimeUtil.unixNanoToKoreanTime(rootSpan.startTimeUnixNano),
                    resource = rootSpan.name,
                    durationMs = TimeUtil.unixNanoToMilliseconds(rootSpan.endTimeUnixNano - rootSpan.startTimeUnixNano),
                    method = rootSpan.getAttributeValue("http.method")?.stringValue,
                    statusCode = rootSpan.getAttributeValue("http.status_code")?.intValue
                )
            }
>>>>>>> trace
        }

        return GetTraceListResponse(traceDtoList)
    }
}