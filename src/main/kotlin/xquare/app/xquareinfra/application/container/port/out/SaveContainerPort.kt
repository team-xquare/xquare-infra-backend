package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity

interface SaveContainerPort {
    fun save(containerJpaEntity: ContainerJpaEntity): ContainerJpaEntity
}