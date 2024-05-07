package xquare.app.xquareinfra.domain.deploy.adapter

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.CreateDeployResponse
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.SimpleDeployListResponse
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.*
import java.util.*

@RequestMapping("/deploy")
@RestController
class DeployWebAdapter(
    private val createDeployUseCase: CreateDeployUseCase,
    private val approveDeployUseCase: ApproveDeployUseCase,
    private val getAllDeployInTeamUseCase: GetAllDeployInTeamUseCase,
    private val getDeployDetailsUseCase: GetDeployDetailsUseCase,
    private val migrationDeployUseCase: MigrationDeployUseCase
) {
    @PostMapping
    fun createDeploy(
        @RequestParam("team_id")
        teamId: UUID,
        @RequestBody
        createDeployRequest: CreateDeployRequest
    ): CreateDeployResponse {
        return createDeployUseCase.createDeploy(teamId, createDeployRequest)
    }

    @PostMapping("/{deployNameEn}/approve")
    fun approveDeploy(
        @PathVariable("deployNameEn")
        deployNameEn: String,
        @RequestBody
        approveDeployRequest: ApproveDeployRequest
    ) {
        approveDeployUseCase.approveDeploy(deployNameEn, approveDeployRequest)
    }

    @GetMapping("/all")
    fun findAllInTeam(
        @RequestParam("teamId", required = true)
        teamId: UUID
    ): SimpleDeployListResponse = getAllDeployInTeamUseCase.getAllDeployInTime(teamId)

    @GetMapping("/{deployId}")
    fun findDeployDetail(
        @PathVariable("deployId", required = true)
        deployId: UUID
    ): DeployDetailsResponse = getDeployDetailsUseCase.getDeployDetails(deployId)

    @PostMapping("/migration")
    fun migrationDeploy() = migrationDeployUseCase.migrationDeploy()
}