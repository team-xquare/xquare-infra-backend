package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.container.port.`in`.ContainerMetricUseCase
import xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.UUID

@Service
class ContainerMetricService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val containerMetricsPort: ContainerMetricsPort
) : ContainerMetricUseCase {
    override fun getContainerCpuUsage(deployId: UUID, environment: ContainerEnvironment): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getCpuUsage(deploy, environment, 3) // TODO :: Duration 사용자 커스텀 가능하도록
    }

    override fun getContainerHttpStatusRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int,
        statusCode: Int
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getHttpStatusRequestsPerMinute(deploy, environment, timeRange, statusCode)
    }

    override fun getContainerHttpRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getHttpRequestsPerMinute(deploy, environment, timeRange)
    }

    override fun getContainerLatency(
        deployId: UUID,
        environment: ContainerEnvironment,
        percent: Int,
        timeRange: Int
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getContainerLatency(deploy, environment, percent / 100.0, timeRange)
    }

    override fun getContainerMemoryUsageUseCase(deployId: UUID, environment: ContainerEnvironment): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getMemoryUsage(deploy, environment, 3) // TODO :: Duration 사용자 커스텀 가능하도록
    }
}