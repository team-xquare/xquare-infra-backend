package xquare.app.xquareinfra.application.team.port.`in`

import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import java.util.*

interface GetTeamDetailUseCase {
    fun getTeamDetail(teamId: UUID): DetailTeamResponse
}