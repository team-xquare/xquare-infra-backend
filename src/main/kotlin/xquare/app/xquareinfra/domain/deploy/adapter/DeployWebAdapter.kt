package xquare.app.xquareinfra.domain.deploy.adapter

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.ApproveDeployUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.CreateDeployUseCase
import java.util.*

@RequestMapping("/deploy")
@RestController
class DeployWebAdapter(
    private val createDeployUseCase: CreateDeployUseCase,
    private val approveDeployUseCase: ApproveDeployUseCase
) {
    @PostMapping
    fun createDeploy(
        @RequestParam("team_id")
        teamId: UUID,
        @RequestBody
        createDeployRequest: CreateDeployRequest
    ) {
        createDeployUseCase.createDeploy(teamId, createDeployRequest)
    }

    @PostMapping("{deployNameEn}/approve")
    fun approveDeploy(
        @PathVariable("deployNameEn")
        deployNameEn: String,
        @RequestBody
        approveDeployRequest: ApproveDeployRequest
    ) {
        approveDeployUseCase.approveDeploy(deployNameEn, approveDeployRequest)
    }
}