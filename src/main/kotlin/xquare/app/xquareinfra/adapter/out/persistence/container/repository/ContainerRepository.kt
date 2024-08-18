package xquare.app.xquareinfra.adapter.out.persistence.container.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import java.util.UUID

interface ContainerRepository : JpaRepository<Container, UUID> {
    fun findByContainerEnvironmentAndDeploy(containerEnvironment: ContainerEnvironment, deploy: Deploy): Container?

    fun findAllByDeploy(deploy: Deploy): List<Container>
}