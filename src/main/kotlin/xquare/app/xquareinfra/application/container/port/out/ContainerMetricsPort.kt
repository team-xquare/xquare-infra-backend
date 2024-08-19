package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface ContainerMetricsPort {
    fun getCpuUsage(deployJpaEntity: DeployJpaEntity, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>>
    fun getMemoryUsage(deployJpaEntity: DeployJpaEntity, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>>
    fun getHttpRequestsPerMinute(deployJpaEntity: DeployJpaEntity, environment: ContainerEnvironment, timeRange: Int): Map<String, Map<String, String>>
    fun getHttpStatusRequestsPerMinute(deployJpaEntity: DeployJpaEntity, environment: ContainerEnvironment, timeRange: Int, statusCode: Int): Map<String, Map<String, String>>
    fun getContainerLatency(
        deployJpaEntity: DeployJpaEntity,
        environment: ContainerEnvironment,
        percent: Double,
        timeRange: Int
    ): Map<String, Map<String, String>>
}