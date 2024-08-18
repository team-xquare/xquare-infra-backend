package xquare.app.xquareinfra.adapter.out.persistence.deploy.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface DeployRepository : JpaRepository<Deploy, UUID> {
    fun existsByDeployName(deployName: String): Boolean

    fun findByDeployName(deployName: String): Deploy?

    fun findAllByTeam(team: Team): List<Deploy>
}