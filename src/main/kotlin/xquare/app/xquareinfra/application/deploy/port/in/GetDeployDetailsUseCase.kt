package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeployDetailsResponse
import java.util.UUID

interface GetDeployDetailsUseCase {
    fun getDeployDetails(deployId: UUID): DeployDetailsResponse
}