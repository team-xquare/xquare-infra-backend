package xquare.app.xquareinfra.application.container.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.application.container.port.out.persistence.repository.ContainerRepository
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: xquare.app.xquareinfra.application.container.port.out.persistence.repository.ContainerRepository
): xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    xquare.app.xquareinfra.application.container.port.out.SaveContainerPort {
    override fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container? =
        containerRepository.findByContainerEnvironmentAndDeploy(containerEnvironment, deploy)

    override fun findAllByDeploy(deploy: Deploy): List<Container> = containerRepository.findAllByDeploy(deploy)

    override fun findAll(): List<Container> = containerRepository.findAll()

    override fun save(container: Container): Container = containerRepository.save(container)
}