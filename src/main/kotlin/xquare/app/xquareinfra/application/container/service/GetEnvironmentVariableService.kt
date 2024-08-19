package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.UUID

@Service
class GetEnvironmentVariableService(
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val findDeployPort: FindDeployPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort
): xquare.app.xquareinfra.application.container.port.`in`.GetEnvironmentVariableUseCase {
    override fun getEnvironmentVariable(deployId: UUID, environment: ContainerEnvironment): Map<String, String> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if(!existsUserTeamPort.existsByTeamAndUser(deploy.teamJpaEntity, user)) {
            throw XquareException.FORBIDDEN
        }

        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        return container.environmentVariable
    }
}