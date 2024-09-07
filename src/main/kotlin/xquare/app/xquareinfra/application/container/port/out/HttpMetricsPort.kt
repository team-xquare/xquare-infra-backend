package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

interface HttpMetricsPort {
    fun getHttpRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, durationMinute: Long): Map<String, Map<String, String>>
    fun getHttpStatusRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, timeRange: Long, statusCode: Int): Map<String, Map<String, String>>
    fun getContainerLatency(
        deploy: Deploy,
        environment: ContainerEnvironment,
        percent: Double,
        durationMinute: Long
    ): Map<String, Map<String, String>>
}