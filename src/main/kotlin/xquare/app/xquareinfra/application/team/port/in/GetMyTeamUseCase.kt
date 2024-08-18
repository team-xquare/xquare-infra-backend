package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList

interface GetMyTeamUseCase {
    fun getMyTeam(): SimpleTeamResponseList
}