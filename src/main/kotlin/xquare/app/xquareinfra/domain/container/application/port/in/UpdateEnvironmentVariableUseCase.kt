package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface UpdateEnvironmentVariableUseCase {
    fun updateEnvironmentVariable(deployName: String, environment: ContainerEnvironment, environmentVariable: Map<String, String>)
}