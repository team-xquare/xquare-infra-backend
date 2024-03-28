package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.ApproveDeployUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Transactional
@Service
class ApproveDeployService(
    private val findDeployPort: FindDeployPort
): ApproveDeployUseCase {
    override fun approveDeploy(deployNameEn: String, req: ApproveDeployRequest) {
        val deploy = findDeployPort.findByDeployName(deployNameEn) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        deploy.updateSecret(req.secretKey)
        deploy.approveDeploy()
    }
}