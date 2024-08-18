package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.UUID

interface UpdateEnvironmentVariableUseCase {
    fun updateEnvironmentVariable(deployId: UUID, environment: ContainerEnvironment, environmentVariable: Map<String, String>)
}