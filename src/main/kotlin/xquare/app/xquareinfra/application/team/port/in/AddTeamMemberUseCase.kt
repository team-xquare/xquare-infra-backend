package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import java.util.UUID

interface AddTeamMemberUseCase {
    fun addTeamMember(addTeamMemberRequest: AddTeamMemberRequest, teamId: UUID)
}