package xquare.app.xquareinfra.domain.team.application.port.`in`

import xquare.app.xquareinfra.domain.team.adapter.dto.response.DetailTeamResponse
import java.util.*

interface GetTeamDetailUseCase {
    fun getTeamDetail(teamId: UUID): DetailTeamResponse
}