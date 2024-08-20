package xquare.app.xquareinfra.adapter.`in`.container

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.request.*
import xquare.app.xquareinfra.adapter.`in`.container.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.application.auth.port.out.SecurityPort
import xquare.app.xquareinfra.application.container.port.`in`.ContainerMetricUseCase
import xquare.app.xquareinfra.application.container.port.`in`.ContainerPipelineUseCase
import xquare.app.xquareinfra.application.container.port.`in`.ContainerUseCase
import xquare.app.xquareinfra.application.container.port.`in`.DockerfileUseCase
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import java.util.*

@RequestMapping("/v2/container")
@RestController
class V2ContainerWebAdapter(
    private val securityPort: SecurityPort,
    private val dockerfileUseCase: DockerfileUseCase,
    private val containerPipelineUseCase: ContainerPipelineUseCase,
    private val containerUseCase: ContainerUseCase,
    private val containerMetricUseCase: ContainerMetricUseCase
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
    ) =
        dockerfileUseCase.createNodeDockerfile(deployId, containerEnvironment, createNodeDockerfileRequest)

    @PostMapping("/node-with-nginx")
    fun createNodeWithNginxDockerfile(
        @RequestParam(name = "deployId", required = true)
        deployId: UUID,
        @RequestParam(name = "environment", required = true)
        containerEnvironment: ContainerEnvironment,
        @RequestBody
        createNodeWithNginxDockerfileRequest: CreateNodeWithNginxDockerfileRequest
    ) = dockerfileUseCase.createNodeWithNginxDockerfile(
        deployId,
        containerEnvironment,
        createNodeWithNginxDockerfileRequest
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

    @GetMapping("/metrics/requests/rate")
    fun getHttpRequestPerMinute(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("timeRange", required = true)
        timeRange: Int
    ): Map<String, Map<String, String>> =
        containerMetricUseCase.getContainerHttpRequestPerMinute(
            deployId,
            environment,
            timeRange,
            securityPort.getCurrentUser()
        )

    @GetMapping("/metrics/http-errors/{statusCode}/rate")
    fun getHttpErrorRequestPerMinute(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestParam("timeRange", required = true)
        timeRange: Int,
        @PathVariable("statusCode", required = true) statusCode: Int
    ): Map<String, Map<String, String>> =
        containerMetricUseCase.getContainerHttpStatusRequestPerMinute(
            deployId,
            environment,
            timeRange,
            statusCode,
            securityPort.getCurrentUser()
        )

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
        containerMetricUseCase.getContainerLatency(
            deployId,
            environment,
            percent,
            timeRange,
            securityPort.getCurrentUser()
        )

    @PostMapping("/webhook")
    fun updateWebhook(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment,
        @RequestBody
        updateContainerWebhookRequest: UpdateContainerWebhookRequest
    ) = containerUseCase.updateContainerWebhook(updateContainerWebhookRequest, deployId, environment)
}