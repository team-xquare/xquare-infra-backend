package xquare.app.xquareinfra.domain.deploy.application.port.out

import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.domain.Team

interface FindDeployPort {
    fun findByDeployName(deployName: String): Deploy?
    fun findAllByTeam(team: Team): List<Deploy>
}