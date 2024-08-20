package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList
import java.util.*

interface TeamUseCase {
    fun create(createTeamRequest: CreateTeamRequest)

    fun deleteTeamMember(deleteTeamMemberRequest: DeleteTeamMemberRequest, teamId: UUID)

    fun addTeamMember(addTeamMemberRequest: AddTeamMemberRequest, teamId: UUID)

    fun getMyTeam(): SimpleTeamResponseList

    fun getTeamDetail(teamId: UUID): DetailTeamResponse
}