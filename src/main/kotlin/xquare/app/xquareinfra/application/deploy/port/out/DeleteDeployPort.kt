package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.adapter.`in`.deploy.dto.request.DeleteContainerRequest
import java.util.*

interface DeleteDeployPort {
    fun deleteDeploy(teamId: UUID, deleteContainerRequest: DeleteContainerRequest)
}