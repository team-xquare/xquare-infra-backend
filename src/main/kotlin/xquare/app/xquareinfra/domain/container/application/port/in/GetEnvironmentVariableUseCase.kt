package xquare.app.xquareinfra.domain.container.application.port.`in`

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

interface GetEnvironmentVariableUseCase {
    fun getEnvironmentVariable(deployName: String, environment: ContainerEnvironment): Map<String, String>
}