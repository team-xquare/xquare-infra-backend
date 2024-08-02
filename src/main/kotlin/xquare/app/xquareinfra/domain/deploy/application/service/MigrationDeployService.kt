package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.MigrationDeployUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.ExistDeployPort
import xquare.app.xquareinfra.domain.deploy.application.port.out.saveDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.deploy.domain.DeployStatus
import xquare.app.xquareinfra.domain.deploy.domain.DeployType
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.external.client.deploy.DeployClient

@Transactional
@Service
class MigrationDeployService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findTeamPort: FindTeamPort,
    private val saveDeployPort: saveDeployPort,
    private val existDeployPort: ExistDeployPort,
    private val deployClient: DeployClient,
    private val existsUserTeamPort: ExistsUserTeamPort
): MigrationDeployUseCase {
    override fun migrationDeploy() {
        val user = readCurrentUserPort.readCurrentUser()

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
                    useRedis = it.useRedis
                )
            )
        }
    }
}