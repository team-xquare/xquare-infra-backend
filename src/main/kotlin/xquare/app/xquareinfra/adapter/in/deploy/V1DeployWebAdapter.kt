package xquare.app.xquareinfra.adapter.`in`.deploy

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.application.deploy.port.`in`.*
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.CreateDeployResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.SimpleDeployListResponse
import java.util.*

@RequestMapping("/v1/deploy")
@RestController
class V1DeployWebAdapter(
    private val createDeployUseCase: CreateDeployUseCase,
    private val deployUseCase: DeployUseCase,
    private val getAllDeployInTeamUseCase: GetAllDeployInTeamUseCase,
    private val getDeployDetailsUseCase: GetDeployDetailsUseCase,
    private val migrationDeployUseCase: MigrationDeployUseCase,
    private val deployMigrationToV2PipelineUseCase: DeployMigrationToV2PipelineUseCase
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
        deployUseCase.approveDeploy(deployNameEn, approveDeployRequest)
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

    @PutMapping("/migration/v2/{deployId}")
    fun migrateToV2(
        @PathVariable("deployId", required = true)
        deployId: UUID
    ) = deployMigrationToV2PipelineUseCase.migrationDeploy(deployId)
}