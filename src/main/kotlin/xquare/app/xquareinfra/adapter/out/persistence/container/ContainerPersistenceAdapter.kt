package xquare.app.xquareinfra.adapter.out.persistence.container

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.container.repository.ContainerRepository
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: ContainerRepository
): xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    xquare.app.xquareinfra.application.container.port.out.SaveContainerPort {
    override fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container? =
        containerRepository.findByContainerEnvironmentAndDeploy(containerEnvironment, deploy)

    override fun findAllByDeploy(deploy: Deploy): List<Container> = containerRepository.findAllByDeploy(deploy)

    override fun findAll(): List<Container> = containerRepository.findAll()

    override fun save(container: Container): Container = containerRepository.save(container)
}