package xquare.app.xquareinfra.adapter.out.persistence.container

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.container.repository.ContainerRepository
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: ContainerRepository
): xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    xquare.app.xquareinfra.application.container.port.out.SaveContainerPort {
    override fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): ContainerJpaEntity? =
        containerRepository.findByContainerEnvironmentAndDeploy(containerEnvironment, deploy)

    override fun findAllByDeploy(deploy: Deploy): List<ContainerJpaEntity> = containerRepository.findAllByDeploy(deploy)

    override fun findAll(): List<ContainerJpaEntity> = containerRepository.findAll()

    override fun save(containerJpaEntity: ContainerJpaEntity): ContainerJpaEntity = containerRepository.save(containerJpaEntity)
}