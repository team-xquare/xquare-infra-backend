package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest

interface CreateTeamUseCase {
    fun create(createTeamRequest: CreateTeamRequest)
}