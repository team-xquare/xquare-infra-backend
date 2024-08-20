package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.container.port.`in`.GetContainerCpuUsageUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.UUID

@Service
class GetContainerCpuUsageService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val containerMetricsPort: xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
) : GetContainerCpuUsageUseCase {
    override fun getContainerCpuUsage(deployId: UUID, environment: ContainerEnvironment): Map<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if (!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containerMetricsPort.getCpuUsage(deploy, environment, 3) // TODO :: Duration 사용자 커스텀 가능하도록
    }
}