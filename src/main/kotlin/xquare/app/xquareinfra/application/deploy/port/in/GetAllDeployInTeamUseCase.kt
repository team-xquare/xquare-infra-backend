package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.SimpleDeployListResponse
import java.util.*

interface GetAllDeployInTeamUseCase {
    fun getAllDeployInTime(teamId: UUID): SimpleDeployListResponse
}