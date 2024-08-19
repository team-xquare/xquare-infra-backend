package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.CreateDeployResponse
import java.util.UUID

interface CreateDeployUseCase {
    fun createDeploy(teamId: UUID, createDeployRequest: CreateDeployRequest): CreateDeployResponse
}