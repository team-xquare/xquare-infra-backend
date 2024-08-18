package xquare.app.xquareinfra.application.team.port.out

import xquare.app.xquareinfra.domain.team.domain.Team
import java.util.UUID

interface FindTeamPort {
    fun findById(teamId: UUID): Team?
    fun findByName(teamName: String): Team?
}