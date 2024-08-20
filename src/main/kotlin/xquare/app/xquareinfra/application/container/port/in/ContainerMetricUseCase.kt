package xquare.app.xquareinfra.application.container.port.`in`

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.UUID

interface ContainerMetricUseCase {
    fun getContainerCpuUsage(deployId: UUID, environment: ContainerEnvironment): Map<String, Map<String, String>>

    fun getContainerHttpRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int
    ): Map<String, Map<String, String>>

    fun getContainerHttpStatusRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int,
        statusCode: Int
    ): Map<String, Map<String, String>>

    fun getContainerLatency(
        deployId: UUID,
        environment: ContainerEnvironment,
        percent: Int,
        timeRange: Int
    ): Map<String, Map<String, String>>

    fun getContainerMemoryUsageUseCase(
        deployId: UUID,
        environment: ContainerEnvironment
    ): Map<String, Map<String, String>>

}