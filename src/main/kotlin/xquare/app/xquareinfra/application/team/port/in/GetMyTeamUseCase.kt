package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.domain.team.adapter.dto.response.SimpleTeamResponseList

interface GetMyTeamUseCase {
    fun getMyTeam(): SimpleTeamResponseList
}