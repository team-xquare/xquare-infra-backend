package xquare.app.xquareinfra.application.deploy.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.CreateDeployRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.CreateDeployResponse
import xquare.app.xquareinfra.application.deploy.port.`in`.CreateDeployUseCase
import xquare.app.xquareinfra.application.deploy.port.out.saveDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.external.deploy.client.DeployClient
import xquare.app.xquareinfra.infrastructure.external.deploy.client.dto.request.FeignCreateDeployRequest
import java.util.*

@Transactional
@Service
class CreateDeployService(
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val saveDeployPort: saveDeployPort,
    private val existDeployPort: ExistDeployPort,
    private val deployClient: DeployClient,
    private val readCurrentUserPort: ReadCurrentUserPort
): CreateDeployUseCase {
    override fun createDeploy(teamId: UUID, req: CreateDeployRequest): CreateDeployResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val user = readCurrentUserPort.readCurrentUser()

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

        val deploy = req.run {
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
                    useRedis = useRedis
                )
            )
        }
        return CreateDeployResponse(deployId = deploy.id!!, teamId = team.id!!)
    }
}