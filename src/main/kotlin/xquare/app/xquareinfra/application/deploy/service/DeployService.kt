package xquare.app.xquareinfra.application.deploy.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.DeleteContainerRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.*
import xquare.app.xquareinfra.adapter.out.external.deploy.client.DeployClient
import xquare.app.xquareinfra.adapter.out.external.deploy.client.dto.request.FeignCreateDeployRequest
import xquare.app.xquareinfra.adapter.out.external.github.client.GithubClient
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DispatchEventRequest
import xquare.app.xquareinfra.adapter.out.external.github.env.GithubProperties
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
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val githubProperties: GithubProperties,
    private val githubClient: GithubClient
): DeployUseCase {

    override fun approveDeploy(deployNameEn: String, req: ApproveDeployRequest) {
        if(req.accessKey != accessKey) {
            throw XquareException.FORBIDDEN
        }
        val deploy = findDeployPort.findByDeployName(deployNameEn) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        saveDeployPort.saveDeploy(deploy.updateSecret(req.secretKey).approveDeploy())

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
                    teamId = team.id!!,
                    secretKey = null,
                    deployStatus = DeployStatus.WAIT_FOR_APPROVE,
                    deployType = deployType,
                    useMysql = useMysql,
                    useRedis = useRedis,
                    v2 = false
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
        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        if(!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }

        return DeployDetailsResponse(
            teamNameKo = team.teamNameKo,
            teamNameEn = team.teamNameEn,
            oneLineDescription = deploy.oneLineDescription,
            repository = deploy.repository,
            projectRootDir = deploy.projectRootDir,
            deployStatus = deploy.deployStatus,
            deployName = deploy.deployName,
            githubFullUrl = "https://github.com/${deploy.organization}/${deploy.repository}",
            isV2 = deploy.v2
        )
    }

    override fun migrationDeploy(user: User) {
        val deployList = deployClient.getAllDeploy(user.email)
        deployList.map {
            val team = findTeamPort.findByName(it.team) ?: throw BusinessLogicException.TEAM_NOT_FOUND

            if(!existsUserTeamPort.existsByTeamIdAndUser(team.id!!, user)) {
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
                    teamId = team.id!!,
                    secretKey = null,
                    deployStatus = if (it.isApproved) DeployStatus.AVAILABLE else DeployStatus.WAIT_FOR_APPROVE,
                    deployType = if (it.type == "be") DeployType.be else DeployType.fe,
                    useMysql = it.useMysql,
                    useRedis = it.useRedis,
                    v2 = false
                )
            )
        }
    }

    override fun deleteDeploy(user: User, deployId: UUID): DeleteContainerResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        if(!existsUserTeamPort.existsByTeamIdAndUser(deploy.teamId, user)) {
            throw XquareException.FORBIDDEN
        }
        val authorization = "Bearer ${githubProperties.token}"
        val accept = "application/vnd.github.v3+json"
        val request = DispatchEventRequest(
            event_type = "delete-service",
            client_payload = mapOf(
                "deployId" to deployId.toString(),
                "deployName" to deploy.deployName,
                "teamId" to deploy.teamId.toString(),
                "organization" to deploy.organization,
                "repository" to deploy.repository
            )
        )

        githubClient.dispatchWorkflowGitops(authorization, accept, request)

        saveDeployPort.deleteDeploy(deploy)

        return DeleteContainerResponse(
            deployId = deployId,
            deployName = deploy.deployName
        )
    }

}