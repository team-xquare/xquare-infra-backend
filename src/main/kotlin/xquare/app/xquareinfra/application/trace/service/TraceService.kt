package xquare.app.xquareinfra.application.trace.service

import okhttp3.internal.notifyAll
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.*
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.application.trace.port.out.DatadogPort
import xquare.app.xquareinfra.application.trace.port.out.FindSpanPort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.Status
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.util.*

@Service
class TraceService(
    private val findDeployPort: FindDeployPort,
    private val findTracePort: FindTracePort,
    private val findSpanPort: FindSpanPort,
    private val serviceMapBuilder: ServiceMapBuilder,
    private val findTeamPort: FindTeamPort,
    private val findContainerPort: FindContainerPort,
    private val datadogPort: DatadogPort
) : TraceUseCase {
    override fun getAllSpansByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRangeSeconds: Long
    ): GetSpanListResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)

        val timeRangeInNanos = TimeUtil.getTimeRangeInNanosSeconds(timeRangeSeconds)

        val spanList = findSpanPort.findSpansByServiceNameInTimeRange(
            serviceName = serviceName,
            startTimeNano = timeRangeInNanos.past,
            endTimeNano = timeRangeInNanos.now
        )

        val spanResponse = spanList.map { span ->
            SpanResponse(
                traceId = span.traceId,
                date = TimeUtil.unixNanoToKoreanTime(span.startTimeUnixNano),
                resource = span.name,
                durationMs = TimeUtil.unixNanoToMilliseconds(span.endTimeUnixNano - span.startTimeUnixNano),
                method = span.getStatusCode()?.toString(),
                statusCode = span.getStatusCode()?.toLong()
            )
        }.sortedByDescending { it.date }

        return GetSpanListResponse(spanResponse)
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
                },
                serviceName = span.getServiceNameInScope()
            )
        }

        return GetTraceDetailResponse(traceList)
    }

    override fun getServiceMap(teamId: UUID, startTimeNano: Long, endTimeNano: Long): ServiceMapResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val deploys = findDeployPort.findAllByTeam(team)

        val teamSpans: MutableList<Span> = mutableListOf()

        deploys.map { deploy ->
            val containers = findContainerPort.findAllByDeploy(deploy)
            containers.map { container ->
                val serviceName = ContainerUtil.getContainerName(deploy, container)
                val spans = findSpanPort.findSpansByServiceNameInTimeRange(
                    serviceName = serviceName,
                    startTimeNano = startTimeNano,
                    endTimeNano = endTimeNano
                )
                teamSpans.addAll(spans)
            }
        }

        return serviceMapBuilder.toServiceMapResponse(teamSpans)
    }

    override fun getServiceEmbedDashboard(deployId: UUID, environment: ContainerEnvironment): String {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)
        val dashboardList = datadogPort.getAllDashboard()

        val dashboard = dashboardList.dashboards.find {
            it.title.startsWith(serviceName)
        } ?: throw BusinessLogicException.DASHBOARD_NOT_FOUND

        val sharedDashboard = datadogPort.createSharedDashboard(dashboard)

        when (sharedDashboard.status) {
            Status.OK -> {
                return sharedDashboard.sharedDashboardResponse.publicUrl
            }
            Status.ERROR -> {
                throw XquareException.INTERNAL_SERVER_ERROR
            }
            Status.CONFLICT -> {
                throw XquareException.CONFLICT
            }
        }
    }
}