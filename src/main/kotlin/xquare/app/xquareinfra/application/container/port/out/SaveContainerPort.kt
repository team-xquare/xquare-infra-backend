package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity

interface SaveContainerPort {
    fun save(container: Container): Container
}