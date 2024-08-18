package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerDeployHistoryUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.domain.container.application.port.out.ContainerDeployHistoryPort
import java.util.*

@Transactional
@Service
class GetContainerDeployHistoryService(
    private val findDeployPort: FindDeployPort,
    private val containerDeployHistoryPort: ContainerDeployHistoryPort
) : GetContainerDeployHistoryUseCase {
    override fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val histories = containerDeployHistoryPort.getContainerDeployHistory(deploy.deployName, containerEnvironment)

        return GetContainerDeployHistoryResponse(histories)
    }
}