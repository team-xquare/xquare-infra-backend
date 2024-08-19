package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface GetEnvironmentVariableUseCase {
    fun getEnvironmentVariable(deployId: UUID, environment: ContainerEnvironment): Map<String, String>
}