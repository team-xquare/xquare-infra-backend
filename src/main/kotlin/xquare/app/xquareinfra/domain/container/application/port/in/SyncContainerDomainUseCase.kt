package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface SyncContainerDomainUseCase {
    fun syncContainerDomain(deployName: String, containerEnvironment: ContainerEnvironment, domain: String)
}