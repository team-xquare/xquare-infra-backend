package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.container.port.`in`.ContainerMetricUseCase
import xquare.app.xquareinfra.application.container.port.out.CpuMemoryMetricsPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.util.*

@Service
class ContainerMetricService(
    private val findDeployPort: FindDeployPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val cpuMemoryMetricsPort: CpuMemoryMetricsPort,
    private val findTracePort: FindTracePort,
    private val traceAnalysisService: TraceAnalysisService
) : ContainerMetricUseCase {
    override fun getContainerCpuUsage(deployId: UUID, environment: ContainerEnvironment, user: User): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if (!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        return cpuMemoryMetricsPort.getCpuUsage(deploy, environment, 180) // TODO :: Duration 사용자 커스텀 가능하도록
    }

    override fun getContainerHttpStatusRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Long,
        statusCode: Int,
        user: User
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if (!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        val timeRangeInNanosMinutes = TimeUtil.getTimeRangeInNanosMinutes(timeRange)

        val traces = findTracePort.findTracesByServiceNameInTimeRange(
            serviceName = ContainerUtil.getContainerName(deploy, environment),
            startTimeNano = timeRangeInNanosMinutes.past,
            endTimeNano = timeRangeInNanosMinutes.now
        )

        return mapOf("0" to traceAnalysisService.analyzeHttpStatusCodes(traces, statusCode))
    }

    override fun getContainerHttpRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Long,
        user: User
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if (!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        val timeRangeInNanosMinutes = TimeUtil.getTimeRangeInNanosMinutes(timeRange)

        val traces = findTracePort.findTracesByServiceNameInTimeRange(
            serviceName = ContainerUtil.getContainerName(deploy, environment),
            startTimeNano = timeRangeInNanosMinutes.past,
            endTimeNano = timeRangeInNanosMinutes.now
        )

        return mapOf("0" to traceAnalysisService.analyzeHttpRequestsPerMinute(traces))
    }

    override fun getContainerLatency(
        deployId: UUID,
        environment: ContainerEnvironment,
        percent: Int,
        timeRange: Long,
        user: User
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if (!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        val timeRangeInNanosMinutes = TimeUtil.getTimeRangeInNanosMinutes(timeRange)

        val traces = findTracePort.findTracesByServiceNameInTimeRange(
            serviceName = ContainerUtil.getContainerName(deploy, environment),
            startTimeNano = timeRangeInNanosMinutes.past,
            endTimeNano = timeRangeInNanosMinutes.now
        )

        return mapOf("0" to traceAnalysisService.analyzeLatency(traces, percent))
    }

    override fun getContainerMemoryUsageUseCase(deployId: UUID, environment: ContainerEnvironment, user: User): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if (!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        return cpuMemoryMetricsPort.getMemoryUsage(deploy, environment, 180) // TODO :: Duration 사용자 커스텀 가능하도록
    }
}