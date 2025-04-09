package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

interface CpuMemoryMetricsPort {
    fun getCpuUsage(deploy: Deploy, environment: ContainerEnvironment, durationMinute: Int): Map<String, Map<String, String>>
    fun getMemoryUsage(deploy: Deploy, environment: ContainerEnvironment, durationMinute: Int): Map<String, Map<String, String>>
}