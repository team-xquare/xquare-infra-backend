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
import xquare.app.xquareinfra.application.team.port.`in`.TeamUseCase
import java.util.UUID

@RequestMapping("/v1/team")
@RestController
class V1TeamWebAdapter(
    private val teamUseCase: TeamUseCase,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createTeam(
        @RequestBody
        req: CreateTeamRequest
    ) = teamUseCase.create(req)

    @GetMapping("/my-team")
    fun getMyTeams(): SimpleTeamResponseList = teamUseCase.getMyTeam()

    @GetMapping("/detail/{teamId}")
    fun getTeamDetail(@PathVariable("teamId") teamId: UUID): DetailTeamResponse =
        teamUseCase.getTeamDetail(teamId)

    @PutMapping("/{teamId}/members")
    fun addTeamMember(
        @RequestBody addTeamMemberRequest: AddTeamMemberRequest,
        @PathVariable("teamId") teamId: UUID
    ) = teamUseCase.addTeamMember(addTeamMemberRequest, teamId)

    @DeleteMapping("/{teamId}/members")
    fun deleteTeamMember(
        @RequestBody deleteTeamMemberRequest: DeleteTeamMemberRequest,
        @PathVariable("teamId") teamId: UUID
    ) = teamUseCase.deleteTeamMember(deleteTeamMemberRequest, teamId)
}