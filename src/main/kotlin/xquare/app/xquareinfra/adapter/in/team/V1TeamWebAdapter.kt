package xquare.app.xquareinfra.adapter.`in`.team

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList
import java.util.UUID

@RequestMapping("/v1/team")
@RestController
class V1TeamWebAdapter(
    private val createTeamUseCase: xquare.app.xquareinfra.application.team.port.`in`.CreateTeamUseCase,
    private val getMyTeamUseCase: xquare.app.xquareinfra.application.team.port.`in`.GetMyTeamUseCase,
    private val getTeamDetailUseCase: xquare.app.xquareinfra.application.team.port.`in`.GetTeamDetailUseCase,
    private val addTeamMemberUseCase: xquare.app.xquareinfra.application.team.port.`in`.AddTeamMemberUseCase,
    private val deleteTeamMemberUseCase: xquare.app.xquareinfra.application.team.port.`in`.DeleteTeamMemberUseCase
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createTeam(
        @RequestBody
        req: CreateTeamRequest
    ) = createTeamUseCase.create(req)

    @GetMapping("/my-team")
    fun getMyTeams(): SimpleTeamResponseList = getMyTeamUseCase.getMyTeam()

    @GetMapping("/detail/{teamId}")
    fun getTeamDetail(@PathVariable("teamId") teamId: UUID): DetailTeamResponse =
        getTeamDetailUseCase.getTeamDetail(teamId)

    @PutMapping("/{teamId}/members")
    fun addTeamMember(
        @RequestBody addTeamMemberRequest: AddTeamMemberRequest,
        @PathVariable("teamId") teamId: UUID
    ) = addTeamMemberUseCase.addTeamMember(addTeamMemberRequest, teamId)

    @DeleteMapping("/{teamId}/members")
    fun deleteTeamMember(
        @RequestBody deleteTeamMemberRequest: DeleteTeamMemberRequest,
        @PathVariable("teamId") teamId: UUID
    ) = deleteTeamMemberUseCase.deleteTeamMember(deleteTeamMemberRequest, teamId)
}