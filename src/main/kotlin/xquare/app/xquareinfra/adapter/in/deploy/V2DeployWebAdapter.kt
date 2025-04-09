package xquare.app.xquareinfra.adapter.`in`.deploy

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.UpdateAppInstallIdRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeleteContainerResponse
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.application.deploy.port.`in`.DeployUseCase
import java.util.*

@RequestMapping("/v2/deploy")
@RestController
class V2DeployWebAdapter(
    private val deployUseCase: DeployUseCase,
    private val securityPort: SecurityPort
) {
    @DeleteMapping("/{deployId}")
    fun deleteContainer(
        @PathVariable
        deployId: UUID,
    ): DeleteContainerResponse {
        return deployUseCase.deleteDeploy(securityPort.getCurrentUser(), deployId)
    }

    @PutMapping("/{deployId}/app-install-id")
    fun updateAppInstallId(
        @PathVariable
        deployId: UUID,
        @RequestBody
        updateAppInstallIdRequest: UpdateAppInstallIdRequest
    ) {
        deployUseCase.updateAppInstallId(
            deployId = deployId,
            appInstallId = updateAppInstallIdRequest.appInstallId,
            user = securityPort.getCurrentUser()
        )
    }
}