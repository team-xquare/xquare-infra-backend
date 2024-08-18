package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.DeployDetailsResponse
import java.util.UUID

interface GetDeployDetailsUseCase {
    fun getDeployDetails(deployId: UUID): DeployDetailsResponse
}