package xquare.app.xquareinfra.domain.team.application.port.`in`

import xquare.app.xquareinfra.domain.team.adapter.dto.request.CreateTeamRequest

interface CreateTeamUseCase {
    fun create(createTeamRequest: CreateTeamRequest)
}