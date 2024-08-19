package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.SimpleDeployListResponse
import java.util.*

interface GetAllDeployInTeamUseCase {
    fun getAllDeployInTime(teamId: UUID): SimpleDeployListResponse
}