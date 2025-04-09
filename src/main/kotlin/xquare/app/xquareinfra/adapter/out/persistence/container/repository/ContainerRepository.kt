package xquare.app.xquareinfra.adapter.out.persistence.container.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import java.util.UUID

interface ContainerRepository : JpaRepository<ContainerJpaEntity, UUID> {
    fun findByContainerEnvironmentAndDeployId(containerEnvironment: ContainerEnvironment, deployId: UUID): ContainerJpaEntity?

    fun findAllByDeployId(deployId: UUID): List<ContainerJpaEntity>
}