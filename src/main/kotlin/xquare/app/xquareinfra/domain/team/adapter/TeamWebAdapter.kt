package xquare.app.xquareinfra.domain.team.adapter

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.domain.team.adapter.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.domain.team.application.port.`in`.CreateTeamUseCase

@RequestMapping("/team")
@RestController
class TeamWebAdapter(
    private val createTeamUseCase: CreateTeamUseCase
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createTeam(
        @RequestBody
        req: CreateTeamRequest
    ) = createTeamUseCase.create(req)
}