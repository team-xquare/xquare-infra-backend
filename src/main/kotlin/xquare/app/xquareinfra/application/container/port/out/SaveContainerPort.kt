package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.domain.Container

interface SaveContainerPort {
    fun save(container: Container): Container
}