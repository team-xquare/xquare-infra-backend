package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class GetContainerDeployHistoryService(
    private val findDeployPort: FindDeployPort,
    private val containerDeployHistoryPort: xquare.app.xquareinfra.application.container.port.out.ContainerDeployHistoryPort
) : xquare.app.xquareinfra.application.container.port.`in`.GetContainerDeployHistoryUseCase {
    override fun getContainerDeployHistory(
        deployId: UUID,
        containerEnvironment: ContainerEnvironment
    ): GetContainerDeployHistoryResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val histories = containerDeployHistoryPort.getContainerDeployHistory(deploy.deployName, containerEnvironment)

        return GetContainerDeployHistoryResponse(histories)
    }
}