package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerHttpStatusRequestPerMinuteUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.domain.container.application.port.out.ContainerMetricsPort
import java.util.*

@Service
class GetContainerHttpStatusRequestPerMinuteService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val containerMetricsPort: ContainerMetricsPort
) : GetContainerHttpStatusRequestPerMinuteUseCase {
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
}