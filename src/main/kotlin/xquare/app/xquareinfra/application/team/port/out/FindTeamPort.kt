package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import java.util.UUID

interface FindTeamPort {
    fun findById(teamId: UUID): TeamJpaEntity?
    fun findByName(teamName: String): TeamJpaEntity?
}