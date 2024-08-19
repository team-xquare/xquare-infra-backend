package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface FindContainerPort {
    fun findByDeployAndEnvironment(deployJpaEntity: DeployJpaEntity, containerEnvironment: ContainerEnvironment): ContainerJpaEntity?
    fun findAllByDeploy(deployJpaEntity: DeployJpaEntity): List<ContainerJpaEntity>
    fun findAll(): List<ContainerJpaEntity>
}