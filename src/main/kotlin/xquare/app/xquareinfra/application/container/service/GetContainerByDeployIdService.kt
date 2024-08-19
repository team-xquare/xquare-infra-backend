package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.SimpleContainerResponse
import xquare.app.xquareinfra.domain.container.model.ContainerStatus
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Service
class GetContainerByDeployIdService(
    private val findDeployPort: FindDeployPort,
    private val findContainerPort: xquare.app.xquareinfra.application.container.port.out.FindContainerPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort
): xquare.app.xquareinfra.application.container.port.`in`.GetContainerByDeployIdUseCase {
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