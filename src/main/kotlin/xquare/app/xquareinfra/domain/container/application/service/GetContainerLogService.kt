package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.container.adapter.dto.response.GetContainerLogResponse
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerLogUseCase
import xquare.app.xquareinfra.domain.container.application.port.out.FindContainerPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.feign.client.log.LogClient
import xquare.app.xquareinfra.infrastructure.feign.client.log.LogUtil
import xquare.app.xquareinfra.infrastructure.feign.client.log.dto.GetLogRequest
import xquare.app.xquareinfra.infrastructure.feign.client.log.dto.QueryDto
import java.time.Instant

@Transactional(readOnly = true)
@Service
class GetContainerLogService(
    private val findContainerPort: FindContainerPort,
    private val findDeployPort: FindDeployPort,
    private val logClient: LogClient
): GetContainerLogUseCase {
    override fun getContainerLog(deployName: String, environment: ContainerEnvironment): GetContainerLogResponse {
        val deploy = findDeployPort.findByDeployName(deployName) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val container = findContainerPort.findByDeployAndEnvironment(deploy, environment)
            ?: throw BusinessLogicException.CONTAINER_NOT_FOUND

        val currentTimeMillis = Instant.now().toEpochMilli()
        val twentyFourHoursAgoMillis = currentTimeMillis - (60 * 60 * 1000)

        val request = GetLogRequest(
            queries = listOf(
                QueryDto(
                    expr = LogUtil.makeLogQuery(
                        team = deploy.team.teamNameEn,
                        containerName = deployName,
                        serviceType = deploy.deployType,
                        envType = environment
                    ),
                    refId = "A",
                    datasource = "loki",
                    hide = false,
                    queryType = "range",
                    intervalMs = 2000,
                    maxDataPoints = 630,
                    maxLines = 3000,
                    legendFormat = "",
                    datasourceId = 3
                )
            ),
            from = twentyFourHoursAgoMillis.toString(),
            to = currentTimeMillis.toString()
        )


        val response = logClient.getLogs(request)
        return GetContainerLogResponse(response.results.a.frames[0].data.values[2])
    }
}