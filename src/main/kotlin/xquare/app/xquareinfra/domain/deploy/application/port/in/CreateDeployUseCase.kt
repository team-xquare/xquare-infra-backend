package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.CreateDeployResponse
import java.util.UUID

interface CreateDeployUseCase {
    fun createDeploy(teamId: UUID, createDeployRequest: CreateDeployRequest): CreateDeployResponse
}