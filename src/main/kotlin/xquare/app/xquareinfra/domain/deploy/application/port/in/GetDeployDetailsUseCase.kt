package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.DeployDetailsResponse
import java.util.UUID

interface GetDeployDetailsUseCase {
    fun getDeployDetails(deployId: UUID): DeployDetailsResponse
}