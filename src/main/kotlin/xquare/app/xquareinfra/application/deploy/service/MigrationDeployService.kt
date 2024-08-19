package xquare.app.xquareinfra.application.deploy.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.deploy.port.`in`.MigrationDeployUseCase
import xquare.app.xquareinfra.application.deploy.port.out.ExistDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.saveDeployPort
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
import xquare.app.xquareinfra.domain.deploy.model.DeployStatus
import xquare.app.xquareinfra.domain.deploy.model.DeployType
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.adapter.out.external.deploy.client.DeployClient

@Transactional
@Service
class MigrationDeployService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val saveDeployPort: saveDeployPort,
    private val existDeployPort: ExistDeployPort,
    private val deployClient: DeployClient,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
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
                DeployJpaEntity(
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