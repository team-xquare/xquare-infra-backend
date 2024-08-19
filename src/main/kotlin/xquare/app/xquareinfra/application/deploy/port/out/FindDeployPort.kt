package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface FindDeployPort {
    fun findByDeployName(deployName: String): DeployJpaEntity?
    fun findAllByTeam(team: Team): List<DeployJpaEntity>
    fun findById(deployId: UUID): DeployJpaEntity?
}