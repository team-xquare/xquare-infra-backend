package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.domain.container.application.port.`in`.SyncContainerUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.application.port.out.SaveContainerPort
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.LocalDateTime
import java.util.UUID

@Transactional
@Service
class SyncContainerService(
    private val saveContainerPort: SaveContainerPort,
    private val findContainerPort: FindContainerPort,
    private val findDeployPort: FindDeployPort
): SyncContainerUseCase {
    override fun syncContainer(syncContainerRequest: SyncContainerRequest) {
        val deploy = findDeployPort.findByDeployName(syncContainerRequest.deployName)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, syncContainerRequest.containerEnvironment)
        var containerId: UUID? = null
        if(container != null) {
            containerId = container.id
        }

        saveContainerPort.save(
            syncContainerRequest.run {
                Container(
                    id = containerId,
                    deploy = deploy,
                    containerEnvironment = containerEnvironment,
                    lastDeploy = LocalDateTime.now(),
                    subDomain = syncContainerRequest.subDomain,
                    environmentVariable = container?.environmentVariable ?: mapOf()
                )
            }
        )
    }
}