package xquare.app.xquareinfra.adapter.out.persistence.container.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import java.util.UUID

interface ContainerRepository : JpaRepository<ContainerJpaEntity, UUID> {
    fun findByContainerEnvironmentAndDeploy(containerEnvironment: ContainerEnvironment, deploy: Deploy): ContainerJpaEntity?

    fun findAllByDeploy(deploy: Deploy): List<ContainerJpaEntity>
}