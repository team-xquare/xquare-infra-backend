package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.SimpleDeployListResponse
import xquare.app.xquareinfra.domain.deploy.adapter.dto.response.SimpleDeployResponse
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.FindAllDeployInTeamUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional(readOnly = true)
@Service
class FindAllDeployInTeamService(
    private val findTeamPort: FindTeamPort,
    private val findDeployPort: FindDeployPort
): FindAllDeployInTeamUseCase {
    override fun findAllDeployInTime(teamId: UUID): SimpleDeployListResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val deployList = findDeployPort.findAllByTeam(team)

        return SimpleDeployListResponse(
            deployList.map {
                SimpleDeployResponse(
                    deployName = it.deployName,
                    repository = it.repository,
                    deployStatus = it.deployStatus
                )
            }
        )
    }
}