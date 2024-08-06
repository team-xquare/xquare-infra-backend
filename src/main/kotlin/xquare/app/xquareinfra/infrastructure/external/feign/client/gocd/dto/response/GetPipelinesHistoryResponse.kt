package xquare.app.xquareinfra.infrastructure.external.client.gocd.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetPipelinesHistoryResponse(
    @JsonProperty("_links")
    val links: Links? = null,
    val pipelines: List<Pipeline>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Links(
    val next: Next? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Next(
    val href: String? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Pipeline(
    val name: String? = null,
    val counter: Int,
    val label: String? = null,
    @JsonProperty("natural_order")
    val naturalOrder: Double? = null,
    @JsonProperty("can_run")
    val canRun: Boolean? = null,
    @JsonProperty("preparing_to_schedule")
    val preparingToSchedule: Boolean? = null,
    val comment: Any? = null,
    @JsonProperty("scheduled_date")
    val scheduledDate: Long? = null,
    @JsonProperty("build_cause")
    val buildCause: BuildCause? = null,
    val stages: List<Stage>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildCause(
    @JsonProperty("trigger_message")
    val triggerMessage: String? = null,
    @JsonProperty("trigger_forced")
    val triggerForced: Boolean? = null,
    val approver: String? = null,
    @JsonProperty("material_revisions")
    val materialRevisions: List<MaterialRevision>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MaterialRevision(
    val changed: Boolean? = null,
    val material: Material? = null,
    val modifications: List<Modification>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Material(
    val name: String? = null,
    val fingerprint: String? = null,
    val type: String? = null,
    val description: String? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Modification(
    val revision: String? = null,
    @JsonProperty("modified_time")
    val modifiedTime: Long? = null,
    @JsonProperty("user_name")
    val userName: String? = null,
    val comment: String? = null,
    @JsonProperty("email_address")
    val emailAddress: Any? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Stage(
    val result: String? = null,
    val status: String? = null,
    @JsonProperty("rerun_of_counter")
    val rerunOfCounter: Any? = null,
    val name: String? = null,
    val counter: String? = null,
    val scheduled: Boolean? = null,
    @JsonProperty("approval_type")
    val approvalType: String? = null,
    @JsonProperty("approved_by")
    val approvedBy: String? = null,
    @JsonProperty("operate_permission")
    val operatePermission: Boolean? = null,
    @JsonProperty("can_run")
    val canRun: Boolean? = null,
    val jobs: List<Job>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Job(
    val name: String? = null,
    @JsonProperty("scheduled_date")
    val scheduledDate: Long? = null,
    val state: String? = null,
    val result: String? = null,
)