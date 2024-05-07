package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.CreateDeployRequest
import java.util.UUID

interface CreateDeployUseCase {
    fun createDeploy(teamId: UUID, createDeployRequest: CreateDeployRequest): UUID
}