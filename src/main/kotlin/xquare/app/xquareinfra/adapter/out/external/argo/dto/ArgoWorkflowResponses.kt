package xquare.app.xquareinfra.adapter.out.external.argo.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ArgoWorkflowListResponse(
    val items: List<ArgoWorkflow> = emptyList()
)

data class ArgoWorkflow(
    val metadata: WorkflowMetadata,
    val spec: WorkflowSpec,
    val status: WorkflowStatus
)

data class WorkflowMetadata(
    val name: String,
    val namespace: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    @JsonProperty("creationTimestamp")
    val creationTimestamp: String
)

data class WorkflowSpec(
    val arguments: WorkflowArguments? = null,
    val templates: List<WorkflowTemplate> = emptyList()
)

data class WorkflowArguments(
    val parameters: List<WorkflowParameter> = emptyList()
)

data class WorkflowParameter(
    val name: String,
    val value: String? = null
)

data class WorkflowTemplate(
    val name: String,
    val steps: List<List<WorkflowStep>>? = null
)

data class WorkflowStep(
    val name: String,
    val template: String,
    val arguments: WorkflowArguments? = null
)

data class WorkflowStatus(
    val phase: String,
    val startedAt: String? = null,
    val finishedAt: String? = null,
    val nodes: Map<String, WorkflowNode> = emptyMap()
)

data class WorkflowNode(
    val id: String,
    val name: String,
    val displayName: String,
    val type: String,
    val phase: String,
    val startedAt: String? = null,
    val finishedAt: String? = null,
    val templateName: String? = null
)

data class ScheduleWorkflowRequest(
    val workflow: WorkflowTemplate,
    val workflowTemplateRef: WorkflowTemplateRef? = null
)

data class WorkflowTemplateRef(
    val name: String
)
