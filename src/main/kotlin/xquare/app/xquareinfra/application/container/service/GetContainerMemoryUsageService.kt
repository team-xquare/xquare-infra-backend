package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.container.port.`in`.GetContainerMemoryUsageUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
import java.util.UUID

@Service
class GetContainerMemoryUsageService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val containerMetricsPort: xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
) : xquare.app.xquareinfra.application.container.port.`in`.GetContainerMemoryUsageUseCase {
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