package xquare.app.xquareinfra.domain.team.application.port.out

import xquare.app.xquareinfra.domain.team.domain.TeamMember

interface SaveTeamMemberPort {
    fun save(teamMember: TeamMember): TeamMember
}