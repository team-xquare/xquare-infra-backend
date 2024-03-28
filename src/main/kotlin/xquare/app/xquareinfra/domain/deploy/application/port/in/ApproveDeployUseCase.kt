package xquare.app.xquareinfra.domain.deploy.application.port.`in`

import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.ApproveDeployRequest

interface ApproveDeployUseCase {
    fun approveDeploy(deployNameEn: String, approveDeployRequest: ApproveDeployRequest)
}