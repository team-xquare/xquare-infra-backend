package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Service
class GetContainerHttpStatusRequestPerMinuteService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val containerMetricsPort: xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
) : xquare.app.xquareinfra.application.container.port.`in`.GetContainerHttpStatusRequestPerMinuteUseCase {
    override fun getContainerHttpStatusRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int,
        statusCode: Int
    ): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.teamJpaEntity, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getHttpStatusRequestsPerMinute(deploy, environment, timeRange, statusCode)
    }
}