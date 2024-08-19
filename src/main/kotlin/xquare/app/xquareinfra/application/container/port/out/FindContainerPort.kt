package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface FindContainerPort {
    fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): ContainerJpaEntity?
    fun findAllByDeploy(deploy: Deploy): List<ContainerJpaEntity>
    fun findAll(): List<ContainerJpaEntity>
}