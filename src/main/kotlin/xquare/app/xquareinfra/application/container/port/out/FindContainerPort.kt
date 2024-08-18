package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface FindContainerPort {
    fun findByDeployAndEnvironment(deploy: Deploy, containerEnvironment: ContainerEnvironment): Container?
    fun findAllByDeploy(deploy: Deploy): List<Container>
    fun findAll(): List<Container>
}