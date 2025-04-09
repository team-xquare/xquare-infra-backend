package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.adapter.out.external.datadog.client.Dashboard
import xquare.app.xquareinfra.adapter.out.external.datadog.client.DashboardList
import xquare.app.xquareinfra.adapter.out.external.datadog.client.SharedDashboardResponse

interface DatadogPort {
    fun getAllDashboard(): DashboardList
    fun createSharedDashboard(dashboard: Dashboard): SharedDashboard
    fun getSharedDashboard(token: String): SharedDashboard
}

data class SharedDashboard(
    val sharedDashboardResponse: SharedDashboardResponse,
    val status: Status
)

enum class Status {
    OK,
    CONFLICT,
    ERROR
}