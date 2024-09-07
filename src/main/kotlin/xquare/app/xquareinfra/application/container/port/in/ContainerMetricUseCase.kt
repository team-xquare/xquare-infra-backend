package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.user.model.User
import java.util.UUID

interface ContainerMetricUseCase {
    fun getContainerCpuUsage(deployId: UUID, environment: ContainerEnvironment, user: User): Map<String, Map<String, String>>

    fun getContainerHttpRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Long,
        user: User
    ): Map<String, Map<String, String>>

    fun getContainerHttpStatusRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Long,
        statusCode: Int,
        user: User
    ): Map<String, Map<String, String>>

    fun getContainerLatency(
        deployId: UUID,
        environment: ContainerEnvironment,
        percent: Int,
        timeRange: Long,
        user: User
    ): Map<String, Map<String, String>>

    fun getContainerMemoryUsageUseCase(
        deployId: UUID,
        environment: ContainerEnvironment,
        user: User
    ): Map<String, Map<String, String>>

}