package xquare.app.xquareinfra.adapter.out.persistence.container

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.container.repository.ContainerRepository
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: ContainerRepository
): FindContainerPort, SaveContainerPort {
    override fun findByDeployAndEnvironment(deployJpaEntity: DeployJpaEntity, containerEnvironment: ContainerEnvironment): ContainerJpaEntity? =
        containerRepository.findByContainerEnvironmentAndDeploy(containerEnvironment, deployJpaEntity)

    override fun findAllByDeploy(deployJpaEntity: DeployJpaEntity): List<ContainerJpaEntity> = containerRepository.findAllByDeploy(deployJpaEntity)

    override fun findAll(): List<ContainerJpaEntity> = containerRepository.findAll()

    override fun save(containerJpaEntity: ContainerJpaEntity): ContainerJpaEntity = containerRepository.save(containerJpaEntity)
}