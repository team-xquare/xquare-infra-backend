package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDetailsResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerDetailsUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.domain.ContainerStatus
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class GetContainerDetailsService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort
): GetContainerDetailsUseCase {
    override fun getContainerDetails(deployId: UUID, environment: ContainerEnvironment): GetContainerDetailsResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        return GetContainerDetailsResponse(
            teamNameEn = deploy.team.teamNameEn,
            deployName = deploy.deployName,
            repository = "${deploy.organization}/${deploy.repository}",
            domain = ContainerUtil.generateDomain(container),
            lastDeploy = container.lastDeploy,
            containerStatus = ContainerStatus.RUNNING, // TODO:: 실제 상태 조회 로직 작성,
            teamNameKo = deploy.team.teamNameKo
            
        )
    }
}