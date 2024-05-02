package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetEnvironmentVariableUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Service
class GetEnvironmentVariableService(
    private val findContainerPort: FindContainerPort,
    private val findDeployPort: FindDeployPort
): GetEnvironmentVariableUseCase {
    override fun getEnvironmentVariable(deployName: String, environment: ContainerEnvironment): Map<String, String> {
        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        return container.environmentVariable
    }
}