package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

interface FindContainerPort {
    fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container?
    fun findAllByDeploy(deploy: Deploy): List<Container>
    fun findAll(): List<Container>
}