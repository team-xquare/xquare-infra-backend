package xquare.app.xquareinfra.application.deploy.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.CreateDeployResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.SimpleDeployListResponse
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.SimpleDeployResponse
import xquare.app.xquareinfra.adapter.out.external.deploy.client.DeployClient
import xquare.app.xquareinfra.adapter.out.external.deploy.client.dto.request.FeignCreateDeployRequest
import xquare.app.xquareinfra.application.container.port.out.FindContainerPort
import xquare.app.xquareinfra.application.deploy.port.`in`.DeployUseCase
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.SaveDeployPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.deploy.model.DeployStatus
import xquare.app.xquareinfra.domain.deploy.model.DeployType
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.integration.vault.VaultService
import java.util.*

@Transactional
@Service
class DeployService(
    @Value("\${secret.projectSecret}")
    private val accessKey: String,
    private val findDeployPort: FindDeployPort,
    private val vaultService: VaultService,
    private val saveDeployPort: SaveDeployPort,
    private val deployClient: DeployClient,
    private val findTeamPort: FindTeamPort,
    private val existDeployPort: ExistDeployPort,
    private val findContainerPort: FindContainerPort,
    private val existsUserTeamPort: ExistsUserTeamPort
): DeployUseCase {

    override fun approveDeploy(deployNameEn: String, req: ApproveDeployRequest) {
        if(req.accessKey != accessKey) {
            throw XquareException.FORBIDDEN
        }
        val deploy = findDeployPort.findByDeployName(deployNameEn) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        deploy.updateSecret(req.secretKey)
        deploy.approveDeploy()

        val path = vaultService.getPath(deploy)
        path.forEach {
            vaultService.addSecret(mapOf("init" to "Please delete this variable"), it)
        }
    }

    override fun createDeploy(teamId: UUID, req: CreateDeployRequest, user: User): CreateDeployResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        if(existDeployPort.existByDeployName(req.deployName)) {
            throw BusinessLogicException.ALREADY_EXISTS_DEPLOY
        }

        val statusCode = deployClient.createDeploy(
            FeignCreateDeployRequest(
                email = user.email,
                nameEn = req.deployName,
                nameKo = req.deployName,
                team = team.teamNameEn,
                repository = "${req.organization}/${req.repository}",
                organization = req.organization,
                type = req.deployType.toString(),
                useMySQL = req.useMysql,
                useRedis = req.useRedis
            )
        ).status()

        if(statusCode >= 400) {
            throw BusinessLogicException.CREATE_DEPLOY_BAD_REQUEST
        }

        val deployJpaEntity = req.run {
            saveDeployPort.saveDeploy(
                Deploy(
                    id = null,
                    deployName = deployName,
                    organization = organization,
                    repository = repository,
                    projectRootDir = projectRootDir,
                    oneLineDescription = oneLineDescription,
                    team = team,
                    secretKey = null,
                    deployStatus = DeployStatus.WAIT_FOR_APPROVE,
                    deployType = deployType,
                    useMysql = useMysql,
                    useRedis = useRedis,
                    isV2 = false
                )
            )
        }
        return CreateDeployResponse(deployId = deployJpaEntity.id!!, teamId = team.id!!)
    }

    override fun migrationDeploy(deployId: UUID) {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        deploy.migrationToV2()

        containers.map {
            val path = vaultService.getPath(deploy, it)
            println(path)
            vaultService.addSecret(it.environmentVariable, vaultService.getPath(deploy, it))
        }
    }

    override fun getAllDeployInTime(teamId: UUID): SimpleDeployListResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val deployList = findDeployPort.findAllByTeam(team)

        return SimpleDeployListResponse(
            teamNameKo = team.teamNameKo,
            deployList = deployList.map {
                SimpleDeployResponse(
                    deployId = it.id!!,
                    deployName = it.deployName,
                    repository = it.repository,
                    deployStatus = it.deployStatus,
                )
            }.sortedByDescending { it.deployName }
        )
    }

    override fun getDeployDetails(deployId: UUID, user: User): DeployDetailsResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if(!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return DeployDetailsResponse(
            teamNameKo = deploy.team.teamNameKo,
            teamNameEn = deploy.team.teamNameEn,
            oneLineDescription = deploy.oneLineDescription,
            repository = deploy.repository,
            projectRootDir = deploy.projectRootDir,
            deployStatus = deploy.deployStatus,
            deployName = deploy.deployName,
            githubFullUrl = "https://github.com/${deploy.organization}/${deploy.repository}",
            isV2 = deploy.isV2
        )
    }

    override fun migrationDeploy(user: User) {
        val deployList = deployClient.getAllDeploy(user.email)
        deployList.map {
            val team = findTeamPort.findByName(it.team) ?: throw BusinessLogicException.TEAM_NOT_FOUND

            if(!existsUserTeamPort.existsByTeamAndUser(team, user)) {
                throw XquareException.FORBIDDEN
            }

            if(existDeployPort.existByDeployName(it.nameEn)) {
                return@map
            }

            saveDeployPort.saveDeploy(
                Deploy(
                    id = null,
                    deployName = it.nameEn,
                    organization = it.organization,
                    repository = it.repository.split("/")[1],
                    projectRootDir = "/",
                    oneLineDescription = "한줄설명을 적어주세요.",
                    team = team,
                    secretKey = null,
                    deployStatus = if (it.isApproved) DeployStatus.AVAILABLE else DeployStatus.WAIT_FOR_APPROVE,
                    deployType = if (it.type == "be") DeployType.be else DeployType.fe,
                    useMysql = it.useMysql,
                    useRedis = it.useRedis,
                    isV2 = false
                )
            )
        }
    }
}