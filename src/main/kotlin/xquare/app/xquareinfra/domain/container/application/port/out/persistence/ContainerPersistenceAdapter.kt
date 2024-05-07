package xquare.app.xquareinfra.domain.container.application.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.application.port.out.SaveContainerPort
import xquare.app.xquareinfra.domain.container.application.port.out.persistence.repository.ContainerRepository
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: ContainerRepository
): FindContainerPort, SaveContainerPort {
    override fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container? =
        containerRepository.findByContainerEnvironmentAndDeploy(containerEnvironment, deploy)

    override fun findAllByDeploy(deploy: Deploy): List<Container> = containerRepository.findAllByDeploy(deploy)

    override fun save(container: Container): Container = containerRepository.save(container)
}