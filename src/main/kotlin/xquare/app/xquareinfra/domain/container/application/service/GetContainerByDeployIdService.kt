package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.adapter.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerByDeployIdUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.domain.ContainerStatus
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Service
class GetContainerByDeployIdService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: FindContainerPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort
): GetContainerByDeployIdUseCase {
    override fun getContainerByDeploy(deployId: UUID): List<SimpleContainerResponse> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val containers = findContainerPort.findAllByDeploy(deploy)

        val user = readCurrentUserPort.readCurrentUser()
        if(!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        return containers.map {
            SimpleContainerResponse(
                containerName = deploy.deployName,
                containerEnvironment = it.containerEnvironment,
                containerStatus = ContainerStatus.RUNNING, // TODO:: 실제 상태 조회 로직 작성
                repository = "${deploy.organization}/${deploy.repository}",
                domain = ContainerUtil.generateDomain(it),
                lastDeploy = it.lastDeploy
            )
        }
    }
}