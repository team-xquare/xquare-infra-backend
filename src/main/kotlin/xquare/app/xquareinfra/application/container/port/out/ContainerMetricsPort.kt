package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

interface ContainerMetricsPort {
    fun getCpuUsage(deploy: Deploy, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>>
    fun getMemoryUsage(deploy: Deploy, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>>
    fun getHttpRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, timeRange: Int): Map<String, Map<String, String>>
    fun getHttpStatusRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, timeRange: Int, statusCode: Int): Map<String, Map<String, String>>
    fun getContainerLatency(
        deploy: Deploy,
        environment: ContainerEnvironment,
        percent: Double,
        timeRange: Int
    ): Map<String, Map<String, String>>
}