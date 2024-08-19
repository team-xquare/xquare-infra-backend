package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment

interface SyncContainerDomainUseCase {
    fun syncContainerDomain(deployName: String, containerEnvironment: ContainerEnvironment, domain: String)
}