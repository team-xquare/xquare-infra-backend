package xquare.app.xquareinfra.adapter.out.external.data

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.adapter.out.external.data.client.PrometheusClient
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.PrometheusDataQueryResponse
import xquare.app.xquareinfra.adapter.out.external.data.util.DataUtil
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.QueryRequest
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.QueryDto
import xquare.app.xquareinfra.application.container.port.out.CpuMemoryMetricsPort
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.time.Instant

@Component
class PrometheusCpuMemoryMetricsAdapter(
    private val prometheusClient: PrometheusClient,
    private val findTeamPort: FindTeamPort
) : CpuMemoryMetricsPort {

    override fun getCpuUsage(
        deploy: Deploy,
        environment: ContainerEnvironment,
        durationMinute: Int
    ): Map<String, Map<String, String>> {
        val team = findTeamPort.findById(deploy.teamId!!) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val query = DataUtil.makeCpuUsageQuery(
            team = team,
            deploy = deploy,
            containerEnvironment = environment
        )
        val queryResponse = executeQuery(query, durationMinute, 2000)
        return formatCpuUsageData(queryResponse)
    }

    override fun getMemoryUsage(
        deploy: Deploy,
        environment: ContainerEnvironment,
        durationMinute: Int
    ): Map<String, Map<String, String>> {
        val team = findTeamPort.findById(deploy.teamId!!) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val query = DataUtil.makeMemoryUsageQuery(
            team = team,
            deploy = deploy,
            containerEnvironment = environment
        )
        val queryResponse = executeQuery(query, durationMinute, 2000)
        return formatMemoryUsageData(queryResponse)
    }

    private fun executeQuery(query: String, durationMinute: Int, intervalMs: Int): PrometheusDataQueryResponse {
        val queryRequest = createQueryRequest(query, durationMinute, intervalMs)
        return prometheusClient.query(
            query = queryRequest.queries[0].expr,
            start = queryRequest.from,
            end = queryRequest.to,
            step = (intervalMs / 1000).toString()
        )
    }

    private fun createQueryRequest(query: String, durationMinute: Int, intervalMs: Int): QueryRequest {
        val currentTimeSeconds = Instant.now().epochSecond
        val durationHoursAgoSeconds = currentTimeSeconds - (durationMinute * 60)
        return QueryRequest(
            queries = listOf(
                QueryDto(
                    expr = query,
                    refId = "A",
                    datasource = "prometheus",
                    hide = false,
                    queryType = "range",
                    intervalMs = intervalMs,
                    maxDataPoints = 630,
                    maxLines = 3000,
                    legendFormat = "",
                    datasourceId = 3
                )
            ),
            from = durationHoursAgoSeconds.toString(),
            to = currentTimeSeconds.toString()
        )
    }

    private fun formatCpuUsageData(queryResponse: PrometheusDataQueryResponse): Map<String, Map<String, String>> {
        val formattedData = DataUtil.formatData(queryResponse)
        return formattedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                String.format("%.2f", usage.toDouble() * 100)
            }
        }
    }

    private fun formatMemoryUsageData(queryResponse: PrometheusDataQueryResponse): Map<String, Map<String, String>> {
        val formattedData = DataUtil.formatData(queryResponse)
        return formattedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                val usageInMB = usage.toDouble() / (1024 * 1024)
                String.format("%.2f", usageInMB)
            }
        }
    }

    private fun formatHttpRequestsData(queryResponse: PrometheusDataQueryResponse, intervalMs: Int): Map<String, Map<String, String>> {
        val rawData = DataUtil.formatData(queryResponse)
        return rawData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                usage?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "0.00"
            }
        }
    }

    private fun formatLatencyData(queryResponse: PrometheusDataQueryResponse, intervalMs: Int): Map<String, Map<String, String>> {
        val rawData = DataUtil.formatData(queryResponse)
        return rawData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                usage?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "0.00"
            }
        }
    }
}