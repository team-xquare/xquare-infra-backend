package xquare.app.xquareinfra.domain.container.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerMemoryUsageUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataClient
import xquare.app.xquareinfra.infrastructure.feign.client.data.DataUtil
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.DataQueryResponse
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.feign.client.data.dto.QueryRequest
import java.time.Instant
import java.util.UUID

@Service
class GetContainerMemoryUsageService(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort
): GetContainerMemoryUsageUseCase {
    override fun getContainerMemoryUsageUseCase(
        deployId: UUID,
        environment: ContainerEnvironment
    ): MutableMap<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if(!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        val memoryUsageReq = createQueryRequest(deploy, environment)
        val queryResponse = queryMemoryUsage(memoryUsageReq)
        val formattedData = DataUtil.formatData(queryResponse)
        formattedData.forEach { (key, timeToUsageMap) ->
            val updatedTimeToUsageMap = timeToUsageMap.mapValues { (_, usage) ->
                val usageInMB = usage.toDouble() / (1024 * 1024)
                String.format("%.2f", usageInMB)
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
                    expr = DataUtil.makeMemoryUsageQuery(
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

    private fun queryMemoryUsage(memoryUsageQueryReq: QueryRequest): DataQueryResponse {
        return dataClient.query(memoryUsageQueryReq)
    }
}