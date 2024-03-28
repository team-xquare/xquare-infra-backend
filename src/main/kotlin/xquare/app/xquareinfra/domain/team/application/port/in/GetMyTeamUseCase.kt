package xquare.app.xquareinfra.domain.team.application.port.`in`

import xquare.app.xquareinfra.domain.team.adapter.dto.response.SimpleTeamResponseList

interface GetMyTeamUseCase {
    fun getMyTeam(): SimpleTeamResponseList
}