package xquare.app.xquareinfra.application.deploy.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.application.deploy.port.`in`.GetDeployDetailsUseCase
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Transactional
@Service
class GetDeployDetailsService(
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
): GetDeployDetailsUseCase {
    override fun getDeployDetails(deployId: UUID): DeployDetailsResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
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
}