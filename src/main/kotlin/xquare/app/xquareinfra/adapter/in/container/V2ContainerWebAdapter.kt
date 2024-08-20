package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v2/container")
@RestController
class V2ContainerWebAdapter(
    private val setContainerConfigUseCase: xquare.app.xquareinfra.application.container.port.`in`.SetContainerConfigUseCase,
    private val dockerfileUseCase: xquare.app.xquareinfra.application.container.port.`in`.DockerfileUseCase,
    private val createNodeWithNginxDockerfileUseCase: xquare.app.xquareinfra.application.container.port.`in`.CreateNodeWithNginxDockerfileUseCase,
    private val createNodeDockerfileUseCase: xquare.app.xquareinfra.application.container.port.`in`.CreateNodeDockerfileUseCase,
    private val containerPipelineUseCase: xquare.app.xquareinfra.application.container.port.`in`.ContainerPipelineUseCase,
    private val syncContainerDomainUseCase: xquare.app.xquareinfra.application.container.port.`in`.SyncContainerDomainUseCase,
    private val getStageLogUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetStageLogUseCase,
    private val getContainerHttpRequestPerMinuteUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerHttpRequestPerMinuteUseCase,
    private val getContainerHttpStatusRequestPerMinuteUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerHttpStatusRequestPerMinuteUseCase,
    private val getContainerLatencyUseCase: xquare.app.xquareinfra.application.container.port.`in`.GetContainerLatencyUseCase,
    private val updateContainerWebhookUseCase: xquare.app.xquareinfra.application.container.port.`in`.UpdateContainerWebhookUseCase
) {
    @PostMapping("/config")
    fun setContainerConfig(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestBody
        setContainerConfigRequest: SetContainerConfigRequest
    ) = setContainerConfigUseCase.setContainerConfig(deployId, setContainerConfigRequest)

    @PostMapping("/gradle")
    fun createGradleDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createGradleDockerfileRequest: CreateGradleDockerfileRequest
    ) = dockerfileUseCase.createGradleDockerfile(deployId, containerEnvironment, createGradleDockerfileRequest)

    @PostMapping("/node")
    fun createNodeDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createNodeDockerfileRequest: CreateNodeDockerfileRequest
    ) = createNodeDockerfileUseCase.createNodeDockerfile(deployId, containerEnvironment, createNodeDockerfileRequest)

    @PostMapping("/node-with-nginx")
    fun createNodeWithNginxDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createNodeWithNginxDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    ) = createNodeWithNginxDockerfileUseCase.createNodeWithNginxDockerfile(deployId, containerEnvironment, createNodeWithNginxDockerfileRequest)

    @GetMapping("/history")
    fun getContainerDeployHistory(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
    ): GetContainerDeployHistoryResponse = containerPipelineUseCase.getContainerDeployHistory(deployId, containerEnvironment)

    @PutMapping("/{deployName}/{containerEnvironment}/sync-domain")
    fun syncContainerDomain(
        @PathVariable("deployName") deployName: String,
        @PathVariable("containerEnvironment") containerEnvironment: ContainerEnvironment,
        @RequestParam("domain") domain: String
    ) = syncContainerDomainUseCase.syncContainerDomain(deployName , containerEnvironment, domain)

    @GetMapping("/{pipelineName}/{pipelineCounter}/stage/{stageName}")
    fun getStageLog(
        @PathVariable("stageName") stageName: String,
        @PathVariable("pipelineCounter") pipelineCounter: Int,
        @PathVariable("pipelineName") pipelineName: String,
    ): String = getStageLogUseCase.getStageLog(pipelineCounter, stageName, pipelineName)

    @GetMapping("/metrics/requests/rate")
    fun getHttpRequestPerMinute(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("timeRange", required = true)
        timeRange: Int
    ): Map<String, Map<String, String>> = getContainerHttpRequestPerMinuteUseCase.getContainerHttpRequestPerMinute(deployId, environment, timeRange)

    @GetMapping("/metrics/http-errors/{statusCode}/rate")
    fun getHttpErrorRequestPerMinute(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("timeRange", required = true)
        timeRange: Int,
        @PathVariable("statusCode", required = true) statusCode: Int
    ): Map<String, Map<String, String>> = getContainerHttpStatusRequestPerMinuteUseCase.getContainerHttpStatusRequestPerMinute(deployId, environment, timeRange, statusCode)

    @GetMapping("/metrics/latency/{percent}")
    fun getLatency(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @PathVariable("percent", required = true) percent: Int,
        @RequestParam("timeRange", required = true)
        timeRange: Int
    ): Map<String, Map<String, String>> =
        getContainerLatencyUseCase.getContainerLatency(deployId, environment, percent, timeRange)

    @PostMapping("/webhook")
    fun updateWebhook(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestBody
        updateContainerWebhookRequest: UpdateContainerWebhookRequest
    ) = updateContainerWebhookUseCase.updateContainerWebhook(updateContainerWebhookRequest, deployId, environment)
}