package xquare.app.xquareinfra.adapter.out.external.argo

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.DeployHistoryResponse
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.StageStatus
import xquare.app.xquareinfra.adapter.out.external.argo.client.ArgoClient
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ScheduleWorkflowRequest
import xquare.app.xquareinfra.adapter.out.external.argo.dto.WorkflowTemplate
import xquare.app.xquareinfra.adapter.out.external.argo.dto.WorkflowTemplateRef
import xquare.app.xquareinfra.application.container.port.out.ContainerPipelinePort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.ZonedDateTime

@Component
class ArgoAdapter(
    private val argoClient: ArgoClient,
    private val findDeployPort: FindDeployPort,
    private val findTeamPort: FindTeamPort,
    @Value("\${argo.auth.token}")
    private val authToken: String
) : ContainerPipelinePort {

    companion object {
        private const val AUTH_HEADER_PREFIX = "Bearer "
        private const val WORKFLOW_TEMPLATE_NAME_FORMAT = "build-%s-%s"
        private const val WORKFLOW_LABEL_DEPLOY_NAME = "xquare/full-name"
    }

    override fun getContainerPipelineHistory(
        deployName: String,
        containerEnvironment: ContainerEnvironment
    ): List<DeployHistoryResponse> {
        // 배포 정보를 통해 팀 정보 가져오기
        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val namespace = ContainerUtil.getNamespaceName(team, containerEnvironment)
        
        val response = argoClient.getWorkflows(
            namespace = namespace,
            token = AUTH_HEADER_PREFIX + authToken
        )

        if (response.statusCode.is4xxClientError) {
            return emptyList()
        }

        return response.body?.items
            ?.filter {
                val labels = it.metadata.labels
                labels[WORKFLOW_LABEL_DEPLOY_NAME] == ContainerUtil.getContainerName(deploy, containerEnvironment)
            }
            ?.mapNotNull { workflow ->
                val annotations = workflow.metadata.annotations
                val createdAt = try {
                    val timestamp = ZonedDateTime.parse(workflow.metadata.creationTimestamp)
                    timestamp.toInstant().toEpochMilli()
                } catch (e: Exception) {
                    0L
                }

                val workflowName = workflow.metadata.name
                val pipelineCounter = workflowName.split("-").lastOrNull()?.toIntOrNull() ?: 0

                // Extract stages from workflow nodes
                val stages = workflow.status.nodes.values
                    .filter { it.type == "Pod" || it.type == "Steps" }
                    .map { node ->
                        StageStatus(
                            name = node.displayName,
                            status = mapArgoStatusToGocdStatus(node.phase)
                        )
                    }

                DeployHistoryResponse(
                    name = annotations["user.name"] ?: "",
                    email = annotations["user.email"] ?: "",
                    scheduledDate = createdAt,
                    commitMessage = annotations["commit.message"] ?: "",
                    stages = stages,
                    pipelineCounter = pipelineCounter,
                    pipelineName = String.format(WORKFLOW_TEMPLATE_NAME_FORMAT, deployName, containerEnvironment.name)
                )
            }
            ?.sortedByDescending { it.scheduledDate }
            ?: emptyList()
    }

    override fun schedulePipeline(deployName: String, containerEnvironment: ContainerEnvironment) {
        val workflowTemplateName = String.format(WORKFLOW_TEMPLATE_NAME_FORMAT, deployName, containerEnvironment.name)

        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val team = findTeamPort.findById(deploy.teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        // 배포 정보를 통해 팀 정보 가져오기
        val namespace = ContainerUtil.getNamespaceName(team, containerEnvironment)
        
        val request = ScheduleWorkflowRequest(
            workflow = WorkflowTemplate(
                name = "",
                steps = null
            ),
            workflowTemplateRef = WorkflowTemplateRef(
                name = workflowTemplateName
            )
        )

        argoClient.submitWorkflow(
            namespace = namespace,
            token = AUTH_HEADER_PREFIX + authToken,
            request = request
        )
    }

    /**
     * Maps Argo workflow phases to GoCD-like status for compatibility
     */
    private fun mapArgoStatusToGocdStatus(argoPhase: String): String {
        return when (argoPhase.lowercase()) {
            "pending" -> "Building"
            "running" -> "Building"
            "succeeded" -> "Passed"
            "failed" -> "Failed"
            "error" -> "Failed"
            "skipped" -> "Cancelled"
            else -> argoPhase
        }
    }
}