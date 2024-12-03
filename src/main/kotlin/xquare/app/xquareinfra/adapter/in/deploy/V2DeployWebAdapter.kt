package xquare.app.xquareinfra.adapter.`in`.deploy

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.DeleteContainerRequest
import xquare.app.xquareinfra.adapter.`in`.deploy.dto.response.DeleteContainerResponse
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.application.deploy.port.`in`.DeployUseCase
import java.util.UUID

@RequestMapping("/v2/deploy")
@RestController
class V2DeployWebAdapter(
    private val deployUseCase: DeployUseCase,
    private val securityPort: SecurityPort
) {
    @DeleteMapping
    fun deleteContainer(
        @RequestParam("team_id")
        teamId: UUID,
        @RequestBody
        deleteContainerRequest: DeleteContainerRequest
    ): DeleteContainerResponse {
        return deployUseCase.deleteDeploy(teamId, securityPort.getCurrentUser(), deleteContainerRequest)
    }
}