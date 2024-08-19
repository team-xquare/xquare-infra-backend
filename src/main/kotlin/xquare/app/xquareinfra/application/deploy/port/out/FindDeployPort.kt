package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.UUID

interface FindDeployPort {
    fun findByDeployName(deployName: String): DeployJpaEntity?
    fun findAllByTeam(teamJpaEntity: TeamJpaEntity): List<DeployJpaEntity>
    fun findById(deployId: UUID): DeployJpaEntity?
}