package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.domain.team.adapter.dto.request.CreateTeamRequest

interface CreateTeamUseCase {
    fun create(createTeamRequest: CreateTeamRequest)
}