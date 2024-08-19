package xquare.app.xquareinfra.adapter.out.persistence.deploy.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface DeployRepository : JpaRepository<DeployJpaEntity, UUID> {
    fun existsByDeployName(deployName: String): Boolean

    fun findByDeployName(deployName: String): DeployJpaEntity?

    fun findAllByTeam(team: Team): List<DeployJpaEntity>
}