package xquare.app.xquareinfra.adapter.out.external.datadog

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.external.datadog.client.*
import xquare.app.xquareinfra.application.trace.port.out.DatadogPort
import xquare.app.xquareinfra.application.trace.port.out.SharedDashboard
import xquare.app.xquareinfra.application.trace.port.out.Status
import xquare.app.xquareinfra.infrastructure.env.datadog.DatadogProperties

@Component
class DatadogAdapter(
    private val datadogClient: DatadogClient,
    private val datadogProperties: DatadogProperties
): DatadogPort {
    override fun getAllDashboard(): DashboardList {
        return datadogClient.getAllDashboards(
            ddApplicationKey = datadogProperties.appKey,
            apiKey = datadogProperties.apiKey
        )
    }

    override fun createSharedDashboard(dashboard: Dashboard): SharedDashboard {
        val responseEntity = datadogClient.createSharedDashboard(
            apiKey = datadogProperties.apiKey,
            ddApplicationKey = datadogProperties.appKey,
            createSharedDashboard = CreateSharedDashboard(
                dashboardType = "custom_timeboard",
                status = "active",
                expiration = null,
                title = dashboard.title,
                dashboardId = dashboard.id,
                shareType = "embed",
                globalTimeSelectableEnabled = true,
                selectableTemplateVars = listOf(
                    SelectableTemplateVar(
                        name = "trace",
                        prefix = "trace",
                        defaultValues = listOf("*"),
                        visibleTags = emptyList()
                    )
                ),
                viewingPreferences = ViewingPreferences(
                    theme = "light"
                ),
                globalTime = GlobalTime(
                    liveSpan = "1h"
                ),
                embeddableDomains = listOf(
                    "https://xquare-infra.xquare.app"
                )
            )
        )


        return if (responseEntity.statusCode.is2xxSuccessful) {
            SharedDashboard(
                status = Status.OK,
                sharedDashboardResponse = responseEntity.body!!
            )

        } else if (responseEntity.statusCode.is4xxClientError){
            SharedDashboard(
                status = Status.CONFLICT,
                sharedDashboardResponse = responseEntity.body!!
            )
        } else {
            SharedDashboard(
                status = Status.ERROR,
                sharedDashboardResponse = responseEntity.body!!
            )
        }
    }

    override fun getSharedDashboard(token: String): SharedDashboard {
        val responseEntity = datadogClient.getSharedDashboard(
            apiKey = datadogProperties.apiKey,
            ddApplicationKey = datadogProperties.appKey,
            token
        )

        return if (responseEntity.statusCode.is2xxSuccessful) {
            SharedDashboard(
                status = Status.OK,
                sharedDashboardResponse = responseEntity.body!!
            )

        } else if (responseEntity.statusCode.is4xxClientError){
            SharedDashboard(
                status = Status.CONFLICT,
                sharedDashboardResponse = responseEntity.body!!
            )
        } else {
            SharedDashboard(
                status = Status.ERROR,
                sharedDashboardResponse = responseEntity.body!!
            )
        }
    }
}