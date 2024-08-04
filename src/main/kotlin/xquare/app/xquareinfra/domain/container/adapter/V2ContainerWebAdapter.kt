package xquare.app.xquareinfra.domain.container.adapter

import org.springframework.web.bind.annotation.*
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateGradleDockerfileRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeDockerfileRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.request.CreateNodeWithNginxDockerfileRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.request.SetContainerConfigRequest
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerDeployHistoryResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.*
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import java.util.*

@RequestMapping("/v2/container")
@RestController
class V2ContainerWebAdapter(
    private val setContainerConfigUseCase: SetContainerConfigUseCase,
    private val createGradleDockerfileUseCase: CreateGradleDockerfileUseCase,
    private val createNodeWithNginxDockerfileUseCase: CreateNodeWithNginxDockerfileUseCase,
    private val createNodeDockerfileUseCase: CreateNodeDockerfileUseCase,
    private val getContainerDeployHistoryUseCase: GetContainerDeployHistoryUseCase,
    private val syncContainerDomainUseCase: SyncContainerDomainUseCase,
    private val getStageLogUseCase: GetStageLogUseCase,
    private val getContainerHttpRequestPerMinuteUseCase: GetContainerHttpRequestPerMinuteUseCase
) {
    @PutMapping("/config")
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
    ) = createGradleDockerfileUseCase.createGradleDockerfile(deployId, containerEnvironment, createGradleDockerfileRequest)

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
    ): GetContainerDeployHistoryResponse = getContainerDeployHistoryUseCase.getContainerDeployHistory(deployId, containerEnvironment)

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

    @GetMapping("/request-per-minute")
    fun getHttpRequestPerMinute(
        @RequestParam("deployId", required = true)
        deployId: UUID,
        @RequestParam("environment", required = true)
        environment: ContainerEnvironment
    ): MutableMap<String, Map<String, String>> = getContainerHttpRequestPerMinuteUseCase.getContainerHttpRequestPerMinute(deployId, environment)
}