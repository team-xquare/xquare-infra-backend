package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.DeployDetailsResponse
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.GetDeployDetailsUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class GetDeployDetailsService(
    private val findDeployPort: FindDeployPort
): GetDeployDetailsUseCase {
    override fun getDeployDetails(deployId: UUID): DeployDetailsResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        return DeployDetailsResponse(
            teamNameKo = deploy.team.teamNameKo,
            teamNameEn = deploy.team.teamNameEn,
            oneLineDescription = deploy.oneLineDescription,
            repository = deploy.repository,
            projectRootDir = deploy.projectRootDir,
            deployStatus = deploy.deployStatus,
            deployName = deploy.deployName
        )
    }
}