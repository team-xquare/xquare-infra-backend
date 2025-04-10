package xquare.app.xquareinfra.adapter.out.external.argo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class ArgoWorkflowListResponse(
    val items: List<ArgoWorkflow> = emptyList()
)

@JsonNaming(SnakeCaseStrategy::class)
data class ArgoWorkflow(
    val metadata: WorkflowMetadata,
    val spec: WorkflowSpec,
    val status: WorkflowStatus
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowMetadata(
    val name: String,
    val namespace: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    @JsonProperty("creationTimestamp")
    val creationTimestamp: String
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowSpec(
    val arguments: WorkflowArguments? = null,
    val templates: List<WorkflowTemplate> = emptyList()
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowArguments(
    val parameters: List<WorkflowParameter> = emptyList()
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowParameter(
    val name: String,
    val value: String? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowTemplate(
    val name: String,
    val steps: List<List<WorkflowStep>>? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowStep(
    val name: String,
    val template: String,
    val arguments: WorkflowArguments? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowStatus(
    val phase: String,
    @JsonProperty("startedAt")
    val startedAt: String? = null,
    @JsonProperty("finishedAt")
    val finishedAt: String? = null,
    val nodes: Map<String, WorkflowNode> = emptyMap()
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowNode(
    val id: String,
    val name: String,
    @JsonProperty("display_name")
    val displayName: String,
    val type: String,
    val phase: String,
    @JsonProperty("startedAt")
    val startedAt: String? = null,
    @JsonProperty("finishedAt")
    val finishedAt: String? = null,
    @JsonProperty("templateName")
    val templateName: String? = null,
    @JsonProperty("boundaryID")
    val boundaryId: String? = null,
    val children: List<String>? = null,
    val message: String? = null,
    @JsonProperty("hostNodeName")
    val hostNodeName: String? = null,
    val progress: String? = null,
    @JsonProperty("resourcesDuration")
    val resourcesDuration: Map<String, Long>? = null,
    val outputs: Map<String, Any>? = null,
    val inputs: WorkflowNodeInputs? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowNodeInputs(
    val parameters: List<WorkflowParameter>? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class ScheduleWorkflowRequest(
    val workflow: WorkflowTemplate,
    val workflowTemplateRef: WorkflowTemplateRef? = null
)

@JsonNaming(SnakeCaseStrategy::class)
data class WorkflowTemplateRef(
    val name: String
)
