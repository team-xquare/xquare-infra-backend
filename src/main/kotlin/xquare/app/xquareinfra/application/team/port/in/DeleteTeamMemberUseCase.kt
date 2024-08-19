package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import java.util.UUID

interface DeleteTeamMemberUseCase {
    fun deleteTeamMember(deleteTeamMemberRequest: DeleteTeamMemberRequest, teamId: UUID)
}