package xquare.app.xquareinfra.application.deploy.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.ApproveDeployRequest
import xquare.app.xquareinfra.application.deploy.port.`in`.ApproveDeployUseCase
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.vault.VaultService

@Transactional
@Service
class ApproveDeployService(
    @Value("\${secret.projectSecret}")
    private val accessKey: String,
    private val findDeployPort: FindDeployPort,
    private val vaultService: VaultService
): ApproveDeployUseCase {

    override fun approveDeploy(deployNameEn: String, req: ApproveDeployRequest) {
        if(req.accessKey != accessKey) {
            throw XquareException.FORBIDDEN
        }
        val deploy = findDeployPort.findByDeployName(deployNameEn) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        deploy.updateSecret(req.secretKey)
        deploy.approveDeploy()

        val path = vaultService.getPath(deploy)
        path.forEach {
            vaultService.addSecret(mapOf("init" to "Please delete this variable"), it)
        }
    }
}