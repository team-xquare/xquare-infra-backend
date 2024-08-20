package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.CreateDeployResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.SimpleDeployListResponse
import java.util.*

interface DeployUseCase {
    fun approveDeploy(deployNameEn: String, approveDeployRequest: ApproveDeployRequest)

    fun createDeploy(teamId: UUID, createDeployRequest: CreateDeployRequest): CreateDeployResponse

    fun migrationDeploy(deployId: UUID)

    fun getAllDeployInTime(teamId: UUID): SimpleDeployListResponse

    fun getDeployDetails(deployId: UUID): DeployDetailsResponse

    fun migrationDeploy()
}