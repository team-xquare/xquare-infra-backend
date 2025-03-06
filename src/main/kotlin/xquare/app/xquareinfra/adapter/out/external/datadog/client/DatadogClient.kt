package xquare.app.xquareinfra.adapter.out.external.datadog.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "datadog-client",
    url = "https://api.us5.datadoghq.com"
)
interface DatadogClient {
    @GetMapping("/api/v1/dashboard")
    fun getAllDashboards(
        @RequestHeader("DD-API-KEY") apiKey: String,
        @RequestHeader("DD-APPLICATION-KEY") ddApplicationKey: String
    ): DashboardList

    @PostMapping("/api/v1/dashboard/public")
    fun createSharedDashboard(
        @RequestHeader("DD-API-KEY") apiKey: String,
        @RequestHeader("DD-APPLICATION-KEY") ddApplicationKey: String,
        @RequestBody createSharedDashboard: CreateSharedDashboard
    ): ResponseEntity<SharedDashboardResponse>
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class DashboardList(
    val dashboards: List<Dashboard>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dashboard(
    val id: String,
    val title: String,
    val description: Any?,
    @JsonProperty("layout_type")
    val layoutType: String,
    val url: String,
    @JsonProperty("is_read_only")
    val isReadOnly: Boolean,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("modified_at")
    val modifiedAt: String,
    @JsonProperty("author_handle")
    val authorHandle: String,
    @JsonProperty("deleted_at")
    val deletedAt: Any?,
)

data class CreateSharedDashboard(
    @JsonProperty("dashboard_type")
    val dashboardType: String,
    val status: String,
    val expiration: Any?,
    val title: String,
    @JsonProperty("dashboard_id")
    val dashboardId: String,
    @JsonProperty("share_type")
    val shareType: String,
    @JsonProperty("global_time_selectable_enabled")
    val globalTimeSelectableEnabled: Boolean,
    @JsonProperty("selectable_template_vars")
    val selectableTemplateVars: List<SelectableTemplateVar>,
    @JsonProperty("viewing_preferences")
    val viewingPreferences: ViewingPreferences,
    @JsonProperty("global_time")
    val globalTime: GlobalTime,
    @JsonProperty("embeddable_domains")
    val embeddableDomains: List<String>,
)

data class SelectableTemplateVar(
    val name: String,
    val prefix: String,
    @JsonProperty("default_values")
    val defaultValues: List<String>,
    @JsonProperty("visible_tags")
    val visibleTags: List<Any?>,
)

data class ViewingPreferences(
    val theme: String,
)

data class GlobalTime(
    @JsonProperty("live_span")
    val liveSpan: String,
)

data class SharedDashboardResponse(
    val author: Author,
    @JsonProperty("dashboard_id")
    val dashboardId: String,
    @JsonProperty("dashboard_type")
    val dashboardType: String,
    val status: String,
    val title: String,
    @JsonProperty("viewing_preferences")
    val viewingPreferences: ViewingPreferences,
    val expiration: Any?,
    @JsonProperty("last_accessed")
    val lastAccessed: Any?,
    @JsonProperty("global_time_selectable_enabled")
    val globalTimeSelectableEnabled: Boolean,
    @JsonProperty("global_time")
    val globalTime: GlobalTime,
    @JsonProperty("selectable_template_vars")
    val selectableTemplateVars: List<SelectableTemplateVar>,
    val token: String,
    @JsonProperty("public_url")
    val publicUrl: String,
    val created: String,
    @JsonProperty("share_type")
    val shareType: String,
    @JsonProperty("share_list")
    val shareList: Any?,
    @JsonProperty("session_duration_in_days")
    val sessionDurationInDays: Any?,
    val invitees: List<Any?>,
    @JsonProperty("embeddable_domains")
    val embeddableDomains: List<String>,
)

data class Author(
    val handle: String,
    val name: String,
)