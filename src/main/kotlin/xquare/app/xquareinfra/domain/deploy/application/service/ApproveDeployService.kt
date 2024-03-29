package xquare.app.xquareinfra.domain.deploy.application.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.deploy.adapter.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.domain.deploy.application.port.`in`.ApproveDeployUseCase
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException

@Transactional
@Service
class ApproveDeployService(
    @Value("\${secret.accessKey}")
    private val accessKey: String,
    private val findDeployPort: FindDeployPort
): ApproveDeployUseCase {

    override fun approveDeploy(deployNameEn: String, req: ApproveDeployRequest) {
        if(req.accessKey != accessKey) {
            throw XquareException.FORBIDDEN
        }
        val deploy = findDeployPort.findByDeployName(deployNameEn) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        deploy.updateSecret(req.secretKey)
        deploy.approveDeploy()
    }
}