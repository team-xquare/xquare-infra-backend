package xquare.app.xquareinfra.application.deploy.port.`in`

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest

interface ApproveDeployUseCase {
    fun approveDeploy(deployNameEn: String, approveDeployRequest: ApproveDeployRequest)
}