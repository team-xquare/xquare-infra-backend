package xquare.app.xquareinfra.application.trace.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.*
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.application.trace.port.out.FindSpanPort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.util.*

@Service
class TraceService(
    private val findDeployPort: FindDeployPort,
    private val findTracePort: FindTracePort,
    private val findSpanPort: FindSpanPort
) : TraceUseCase {
    override fun getRootSpanByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRangeSeconds: Long
    ): GetRootSpanListResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)

        val timeRangeInNanos = TimeUtil.getTimeRangeInNanosSeconds(timeRangeSeconds)

        val rootSpanList = findSpanPort.findRootSpansByServiceName(
            serviceName = serviceName,
            startTimeNano = timeRangeInNanos.past,
            endTimeNano = timeRangeInNanos.now
        )

        val rootSpanResponse = rootSpanList.map { span ->
            RootSpanResponse(
                traceId = span.traceId,
                date = TimeUtil.unixNanoToKoreanTime(span.startTimeUnixNano),
                resource = span.name,
                durationMs = TimeUtil.unixNanoToMilliseconds(span.endTimeUnixNano - span.startTimeUnixNano),
                method = span.getStatusCode()?.toString(),
                statusCode = span.getStatusCode()?.toLong()
            )
        }.sortedByDescending { it.date }

        return GetRootSpanListResponse(rootSpanResponse)
    }

    override fun getTraceDetail(traceId: String): GetTraceDetailResponse {
        val trace = findTracePort.findTraceById(traceId)
            ?: throw BusinessLogicException.TRACE_NOT_FOUND

        val traceList = trace.sortedByAscendingDate().map { span ->
            SpanDetailResponse(
                parentSpanId = span.parentSpanId,
                traceId = span.traceId,
                spanId = span.spanId,
                name = span.name,
                startTimeUnixNano = span.startTimeUnixNano,
                endTimeUnixNano = span.endTimeUnixNano,
                attributes = span.attributes,
                events = span.events.map { event->
                    SpanEventResponse(
                        timeUnixNano = event.timeUnixNano,
                        name = event.name,
                        attributes = event.attributes
                    )
                }
            )
        }

        return GetTraceDetailResponse(traceList)
    }
}