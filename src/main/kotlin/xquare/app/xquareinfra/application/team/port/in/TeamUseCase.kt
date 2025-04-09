package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList
import xquare.app.xquareinfra.domain.user.model.User
import java.util.*

interface TeamUseCase {
    fun create(createTeamRequest: CreateTeamRequest, user: User)

    fun deleteTeamMember(deleteTeamMemberRequest: DeleteTeamMemberRequest, teamId: UUID, user: User)

    fun addTeamMember(addTeamMemberRequest: AddTeamMemberRequest, teamId: UUID, user: User)

    fun getMyTeam(user: User): SimpleTeamResponseList

    fun getTeamDetail(teamId: UUID, user: User): DetailTeamResponse
}