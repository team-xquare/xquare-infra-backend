package xquare.app.xquareinfra.domain.container.application.service

import okhttp3.internal.format
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.container.application.port.`in`.GetContainerHttpRequestPerMinuteUseCase
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.application.port.out.FindDeployPort
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.external.client.data.DataClient
import xquare.app.xquareinfra.infrastructure.external.client.data.DataUtil
import xquare.app.xquareinfra.infrastructure.external.client.data.dto.DataQueryResponse
import xquare.app.xquareinfra.infrastructure.external.client.data.dto.QueryDto
import xquare.app.xquareinfra.infrastructure.external.client.data.dto.QueryRequest
import java.time.Instant
import java.util.*

@Service
class GetContainerHttpRequestPerMinuteService(
    private val dataClient: DataClient,
    private val findDeployPort: FindDeployPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort
) : GetContainerHttpRequestPerMinuteUseCase{
    override fun getContainerHttpRequestPerMinute(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRange: Int
    ): MutableMap<String, Map<String, String>> {
        val deploy = findDeployPort.findById(deployId)
            ?: throw BusinessLogicException.DEPLOY_NOT_FOUND

        val user = readCurrentUserPort.readCurrentUser()
        if(!existsUserTeamPort.existsByTeamAndUser(deploy.team, user)) {
            throw XquareException.FORBIDDEN
        }

        val queryReq = createQueryRequest(deploy, environment, timeRange)
        val queryResponse = queryHttpRequestPerMinute(queryReq)
        println("queryResponse : $queryResponse")
        val formattedData = DataUtil.formatData(queryResponse)
        println("formattedData: $formattedData")
        formattedData.forEach { (key, timeToUsageMap) ->
            val updatedTimeToUsageMap = timeToUsageMap.mapValues { (_, usage) ->
                String.format("%.2f", usage.toDouble())
            }
            formattedData[key] = updatedTimeToUsageMap
        }

        return formattedData
    }

    private fun createQueryRequest(deploy: Deploy, environment: ContainerEnvironment, timeRange: Int): QueryRequest {
        val currentTimeMillis = Instant.now().toEpochMilli()
        val timeRangeMinuteAgoMillis = currentTimeMillis - (timeRange * 60 * 1000)
        return QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = DataUtil.makeRequestPerMinuteQuery(
                        team = deploy.team.teamNameEn,
                        containerName = deploy.deployName,
                        serviceType = deploy.deployType,
                        envType = environment,
                        isV2 = deploy.isV2
                    ),
                    refId = "A",
                    datasource = "prometheus",
                    hide = false,
                    queryType = "range",
                    intervalMs = 60000,
                    maxDataPoints = 630,
                    maxLines = 3000,
                    legendFormat = "",
                    datasourceId = 3
                )
            ),
            from = timeRangeMinuteAgoMillis.toString(),
            to = currentTimeMillis.toString()
        )
    }

    private fun queryHttpRequestPerMinute(httpRequestPerMinuteQueryReq: QueryRequest): DataQueryResponse {
        return dataClient.query(httpRequestPerMinuteQueryReq)
    }
}