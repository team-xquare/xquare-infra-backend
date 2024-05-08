package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerCpuUsageUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataClient
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataUtil
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.DataQueryResponse
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryRequest
import java.time.Instant

@Service
class GetContainerCpuUsageService(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort
): GetContainerCpuUsageUseCase {
    override fun getContainerCpuUsage(deployName: String, environment: ContainerEnvironment): MutableMap<String, Map<String, String>> {
        val deploy = findDeployPort.findByDeployName(deployName)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val cpuUsageQueryReq = createQueryRequest(deploy, environment)
        val queryResponse = queryCpuUsage(cpuUsageQueryReq)
        val formattedData = DataUtil.formatData(queryResponse)
        formattedData.forEach { (key, timeToUsageMap) ->
            val updatedTimeToUsageMap = timeToUsageMap.mapValues { (_, usage) ->
                String.format("%.2f%%", usage.toDouble() * 100)
            }
            formattedData[key] = updatedTimeToUsageMap
        }

        return formattedData
    }

    private fun createQueryRequest(deploy: Deploy, environment: ContainerEnvironment): QueryRequest {
        val currentTimeMillis = Instant.now().toEpochMilli()
        val threeHoursAgoMillis = currentTimeMillis - (3 * 60 * 60 * 1000)
        return QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = DataUtil.makeCpuUsageQuery(
                        team = deploy.team.teamNameEn,
                        containerName = deploy.deployName,
                        serviceType = deploy.deployType,
                        envType = environment
                    ),
                    refId = "A",
                    datasource = "prometheus",
                    hide = false,
                    queryType = "range",
                    intervalMs = 2000,
                    maxDataPoints = 630,
                    maxLines = 3000,
                    legendFormat = "",
                    datasourceId = 3
                )
            ),
            from = threeHoursAgoMillis.toString(),
            to = currentTimeMillis.toString()
        )
    }

    private fun queryCpuUsage(cpuUsageQueryReq: QueryRequest): DataQueryResponse {
        return dataClient.query(cpuUsageQueryReq)
    }
}