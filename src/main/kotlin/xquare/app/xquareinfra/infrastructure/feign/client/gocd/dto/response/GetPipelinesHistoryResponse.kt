package xquare.app.xquareinfra.infrastructure.feign.client.gocd.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GetPipelinesHistoryResponse(
    @JsonProperty("_links")
    val links: Links,
    val pipelines: List<Pipeline>,
)

data class Links(
    val next: Next,
)

data class Next(
    val href: String,
)

data class Pipeline(
    val name: String,
    val counter: Long,
    val label: String,
    @JsonProperty("natural_order")
    val naturalOrder: Double,
    @JsonProperty("can_run")
    val canRun: Boolean,
    @JsonProperty("preparing_to_schedule")
    val preparingToSchedule: Boolean,
    val comment: Any?,
    @JsonProperty("scheduled_date")
    val scheduledDate: Long,
    @JsonProperty("build_cause")
    val buildCause: BuildCause,
    val stages: List<Stage>,
)

data class BuildCause(
    @JsonProperty("trigger_message")
    val triggerMessage: String,
    @JsonProperty("trigger_forced")
    val triggerForced: Boolean,
    val approver: String,
    @JsonProperty("material_revisions")
    val materialRevisions: List<MaterialRevision>,
)

data class MaterialRevision(
    val changed: Boolean,
    val material: Material,
    val modifications: List<Modification>,
)

data class Material(
    val name: String,
    val fingerprint: String,
    val type: String,
    val description: String,
)

data class Modification(
    val revision: String,
    @JsonProperty("modified_time")
    val modifiedTime: Long,
    @JsonProperty("user_name")
    val userName: String,
    val comment: String,
    @JsonProperty("email_address")
    val emailAddress: Any?,
)

data class Stage(
    val result: String,
    val status: String,
    @JsonProperty("rerun_of_counter")
    val rerunOfCounter: Any?,
    val name: String,
    val counter: String,
    val scheduled: Boolean,
    @JsonProperty("approval_type")
    val approvalType: String,
    @JsonProperty("approved_by")
    val approvedBy: String,
    @JsonProperty("operate_permission")
    val operatePermission: Boolean,
    @JsonProperty("can_run")
    val canRun: Boolean,
    val jobs: List<Job>,
)

data class Job(
    val name: String,
    @JsonProperty("scheduled_date")
    val scheduledDate: Long,
    val state: String,
    val result: String,
)

