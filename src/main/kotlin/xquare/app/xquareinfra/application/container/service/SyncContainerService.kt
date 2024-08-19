package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.SyncContainerRequest
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.LocalDateTime
import java.util.UUID

@Transactional
@Service
class SyncContainerService(
    private val saveContainerPort: xquare.app.xquareinfra.application.container.port.out.SaveContainerPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val findDeployPort: FindDeployPort
): xquare.app.xquareinfra.application.container.port.`in`.SyncContainerUseCase {
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
                ContainerJpaEntity(
                    id = containerId,
                    deployJpaEntity = deploy,
                    containerEnvironment = containerEnvironment,
                    lastDeploy = LocalDateTime.now(),
                    subDomain = syncContainerRequest.subDomain,
                    environmentVariable = container?.environmentVariable ?: mapOf()
                )
            }
        )
    }
}