package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.application.container.port.`in`.ContainerPipelineUseCase
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.application.container.port.`in`.DockerfileUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v2/container")
@RestController
class V2ContainerWebAdapter(
    private val dockerfileUseCase: DockerfileUseCase,
    private val containerPipelineUseCase: ContainerPipelineUseCase,
    private val containerUseCase: ContainerUseCase
) {
    @PostMapping("/config")
    fun setContainerConfig(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestBody
        setContainerConfigRequest: SetContainerConfigRequest
    ) = containerUseCase.setContainerConfig(deployId, setContainerConfigRequest)

    @PostMapping("/gradle")
    fun createGradleDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createGradleConfigRequest: CreateGradleConfigRequest
    ) = dockerfileUseCase.createGradleDockerfile(deployId, containerEnvironment, createGradleConfigRequest)

    @PostMapping("/node")
    fun createNodeDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createNodeConfigRequest: CreateNodeConfigRequest
    ) =
        dockerfileUseCase.createNodeDockerfile(deployId, containerEnvironment, createNodeConfigRequest)

    @PostMapping("/node-with-nginx")
    fun createNodeWithNginxDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createNodeWithNginxConfigRequest: CreateNodeWithNginxConfigRequest
    ) = dockerfileUseCase.createNodeWithNginxDockerfile(
        deployId,
        containerEnvironment,
        createNodeWithNginxConfigRequest
    )

    @GetMapping("/history")
    fun getContainerDeployHistory(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
    ): GetContainerDeployHistoryResponse =
        containerPipelineUseCase.getContainerDeployHistory(deployId, containerEnvironment)

    @PutMapping("/{deployName}/{containerEnvironment}/sync-domain")
    fun syncContainerDomain(
        @PathVariable("deployName") deployName: String,
        @PathVariable("containerEnvironment") containerEnvironment: ContainerEnvironment,
        @RequestParam("domain") domain: String
    ) =
        containerUseCase.syncContainerDomain(deployName , containerEnvironment, domain)

    @GetMapping("/{pipelineName}/{pipelineCounter}/stage/{stageName}")
    fun getStageLog(
        @PathVariable("stageName") stageName: String,
        @PathVariable("pipelineCounter") pipelineCounter: Int,
        @PathVariable("pipelineName") pipelineName: String,
    ): String = containerPipelineUseCase.getStageLog(pipelineCounter, stageName, pipelineName)


    @PostMapping("/webhook")
    fun updateWebhook(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestBody
        updateContainerWebhookRequest: UpdateContainerWebhookRequest
    ) = containerUseCase.updateContainerWebhook(updateContainerWebhookRequest, deployId, environment)

    @PostMapping("/pipelines/schedule")
    fun schedulePipeline(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ) = containerPipelineUseCase.schedulePipeline(deployId, environment)
}