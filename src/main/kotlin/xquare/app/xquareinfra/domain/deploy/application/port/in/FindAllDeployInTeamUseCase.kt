package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.SimpleDeployListResponse
import java.util.*

interface FindAllDeployInTeamUseCase {
    fun findAllDeployInTime(teamId: UUID): SimpleDeployListResponse
}