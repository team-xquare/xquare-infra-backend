package xquare.app.xquareinfra.infrastructure.external.prometheus

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.infrastructure.external.data.client.DataClient
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.DataQueryResponse
import xquare.app.xquareinfra.infrastructure.external.data.util.DataUtil
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.QueryRequest
import xquare.app.xquareinfra.infrastructure.external.data.client.dto.QueryDto
import java.time.Instant

@Component
class PrometheusContainerMetricsAdapter(
    private val dataClient: DataClient
) : xquare.app.xquareinfra.application.container.port.out.ContainerMetricsPort {

    override fun getCpuUsage(deploy: Deploy, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>> {
        val query = DataUtil.makeCpuUsageQuery(
            deploy = deploy,
            containerEnvironment = environment
        )
        val queryResponse = executeQuery(query, duration, 2000)
        return formatCpuUsageData(queryResponse)
    }

    override fun getMemoryUsage(deploy: Deploy, environment: ContainerEnvironment, duration: Int): Map<String, Map<String, String>> {
        val query = DataUtil.makeMemoryUsageQuery(
            deploy = deploy,
            containerEnvironment = environment
        )
        val queryResponse = executeQuery(query, duration, 2000)
        return formatMemoryUsageData(queryResponse)
    }

    override fun getHttpRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, timeRange: Int): Map<String, Map<String, String>> {
        val query = DataUtil.makeRequestPerMinuteQuery(deploy, environment)
        val queryResponse = executeQuery(query, timeRange, 2000)
        return formatHttpRequestsData(queryResponse)
    }

    override fun getHttpStatusRequestsPerMinute(deploy: Deploy, environment: ContainerEnvironment, timeRange: Int, statusCode: Int): Map<String, Map<String, String>> {
        val query = DataUtil.makeHttpStatusRequestPerMinuteQuery(deploy, environment, statusCode)
        val queryResponse = executeQuery(query, timeRange, 2000)
        return formatHttpRequestsData(queryResponse)
    }

    override fun getContainerLatency(
        deploy: Deploy,
        environment: ContainerEnvironment,
        percent: Double,
        timeRange: Int
    ): Map<String, Map<String, String>> {
        val query = DataUtil.makeGetLatencyPerMinuteQuery(deploy, environment, percent)
        val queryResponse = executeQuery(query, timeRange, 20000)
        return formatLatencyData(queryResponse)
    }

    private fun executeQuery(query: String, duration: Int, intervalMs: Int): DataQueryResponse {
        val queryRequest = createQueryRequest(query, duration, intervalMs)
        return dataClient.query(queryRequest)
    }

    private fun createQueryRequest(query: String, duration: Int, intervalMs: Int): QueryRequest {
        val currentTimeMillis = Instant.now().toEpochMilli()
        val durationHoursAgoMillis = currentTimeMillis - (duration * 60 * 60 * 1000)
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
            from = durationHoursAgoMillis.toString(),
            to = currentTimeMillis.toString()
        )
    }

    private fun formatCpuUsageData(queryResponse: DataQueryResponse): Map<String, Map<String, String>> {
        val formattedData = DataUtil.formatData(queryResponse)
        return formattedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                String.format("%.2f", usage.toDouble() * 100)
            }
        }
    }

    private fun formatMemoryUsageData(queryResponse: DataQueryResponse): Map<String, Map<String, String>> {
        val formattedData = DataUtil.formatData(queryResponse)
        return formattedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                val usageInMB = usage.toDouble() / (1024 * 1024)
                String.format("%.2f", usageInMB)
            }
        }
    }

    private fun formatHttpRequestsData(queryResponse: DataQueryResponse): Map<String, Map<String, String>> {
        val rawData = DataUtil.formatData(queryResponse)
        val aggregatedData = DataUtil.aggregateDataToMinute(rawData, 20)
        return aggregatedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                usage?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "0.00"
            }
        }
    }

    private fun formatLatencyData(queryResponse: DataQueryResponse): Map<String, Map<String, String>> {
        val rawData = DataUtil.formatData(queryResponse)
        val aggregatedData = DataUtil.aggregateDataToMinute(rawData, 20)
        return aggregatedData.mapValues { (_, timeToUsageMap) ->
            timeToUsageMap.mapValues { (_, usage) ->
                usage?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "0.00"
            }
        }
    }
}