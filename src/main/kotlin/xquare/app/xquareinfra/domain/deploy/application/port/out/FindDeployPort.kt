package xquare.app.xquareinfra.domain.deploy.application.port.out

import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface FindDeployPort {
    fun findByDeployName(deployName: String): Deploy?
    fun findAllByTeam(team: Team): List<Deploy>
    fun findById(deployId: UUID): Deploy?
}