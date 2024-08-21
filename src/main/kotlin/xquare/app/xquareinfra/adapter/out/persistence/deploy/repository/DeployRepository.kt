package xquare.app.xquareinfra.adapter.out.persistence.deploy.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.UUID

interface DeployRepository : JpaRepository<DeployJpaEntity, UUID> {
    fun existsByDeployName(deployName: String): Boolean

    fun findByDeployName(deployName: String): DeployJpaEntity?

    fun findAllByTeamId(teamId: UUID): List<DeployJpaEntity>
}