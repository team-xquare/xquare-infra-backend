package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.team.model.Team
import java.util.UUID

interface FindDeployPort {
    fun findByDeployName(deployName: String): Deploy?
    fun findAllByTeam(team: Team): List<Deploy>
    fun findById(deployId: UUID): Deploy?
}