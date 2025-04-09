package xquare.app.xquareinfra.adapter.out.persistence.container

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.container.repository.ContainerRepository
import xquare.app.xquareinfra.adapter.out.persistence.deploy.DeployMapper
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.container.port.out.SaveContainerPort
import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

@Component
class ContainerPersistenceAdapter(
    private val containerRepository: ContainerRepository,
    private val containerMapper: ContainerMapper,
    private val deployMapper: DeployMapper
): FindContainerPort, SaveContainerPort {
    override fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container? =
        containerRepository.findByContainerEnvironmentAndDeployId(containerEnvironment, deployMapper.toEntity(deploy).id!!)?.let { containerMapper.toModel(it) }

    override fun findAllByDeploy(deploy: Deploy): List<Container> =
        containerRepository.findAllByDeployId(deployMapper.toEntity(deploy).id!!).map { containerMapper.toModel(it) }

    override fun findAll(): List<Container> = containerRepository.findAll().map { containerMapper.toModel(it) }

    override fun save(container: Container): Container = containerMapper.toModel(containerRepository.save(containerMapper.toEntity(container)))
}