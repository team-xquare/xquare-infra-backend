package xquare.app.xquareinfra.adapter.out.external.data.util

import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.PrometheusDataQueryResponse
import xquare.app.xquareinfra.adapter.out.external.data.client.dto.DsDataFrame
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.team.model.Team
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DataUtil {
    fun extractOrganization(githubUrl: String): String {
        val parts = githubUrl.split("/")
        return if (parts.size >= 4) {
            parts[3]
        } else {
            ""
        }
    }

    fun getRepository(githubUrl: String): String {
        val parts = githubUrl.split("/")
        return if (parts.size >= 4) {
            "${parts[3]}/${parts[4]}"
        } else {
            ""
        }
    }

    fun aggregateDataToMinute(rawData: MutableMap<String, Map<String, String>>, intervalMs: Int): MutableMap<String, Map<String, String>> {
        val aggregatedData = mutableMapOf<String, MutableMap<String, Double>>()
        val entriesPerMinute = 60000 / intervalMs

        rawData.forEach { (key, timeToUsageMap) ->
            val aggregatedTimeToUsageMap = mutableMapOf<String, Double>()

            timeToUsageMap.entries.groupBy { it.key.substring(0, 16) }
                .forEach { (minute, entries) ->
                    val sum = entries.take(entriesPerMinute).sumOf { it.value.toDouble() }
                    aggregatedTimeToUsageMap[minute] = sum
                }

            aggregatedData[key] = aggregatedTimeToUsageMap
        }

        return aggregatedData.mapValues { (_, value) -> value.mapValues { it.value.toString() } }.toMutableMap()
    }

    fun makeLogQuery(
        team: Team,
        deploy: Deploy,
        containerEnvironment: ContainerEnvironment
    ): String {
        val namespace = ContainerUtil.getNamespaceName(team, containerEnvironment)
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)
        val response = "{job=\"$namespace/$fullName\", container=~\"${fullName}\"} |~ \"(?i)\" \n"
        return response
    }

    fun makeCpuUsageQuery(team: Team, deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)
        val namespace = ContainerUtil.getNamespaceName(team, containerEnvironment)

        return """
            sum(
                node_namespace_pod_container:container_cpu_usage_seconds_total:sum_irate{
                    cluster="", 
                    namespace="$namespace"
                }
                * on(namespace, pod)
                group_left(workload, workload_type)
                namespace_workload_pod:kube_pod_owner:relabel{
                    cluster="", 
                    namespace="$namespace", 
                    workload="$fullName", 
                    workload_type=~"deployment"
                }
            ) by (pod)
        """.replace("\\s".toRegex(), "")
    }

    fun makeMemoryUsageQuery(team: Team, deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)
        val namespace = ContainerUtil.getNamespaceName(team, containerEnvironment)

        return """
            sum(
                container_memory_working_set_bytes{
                    cluster="", 
                    namespace="$namespace", 
                    container!="", 
                    image!=""
                }
                * on(namespace, pod)
                group_left(workload, workload_type)
                namespace_workload_pod:kube_pod_owner:relabel{
                    cluster="", 
                    namespace="$namespace", 
                    workload="$fullName", 
                    workload_type=~"deployment"
                }
            ) by (pod)
        """.replace("\\s".toRegex(), "")
    }





    fun makeRequestPerMinuteQuery(deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)
        val response = """
            sum(
                rate(
                    http_server_duration_milliseconds_count{
                        exported_job="$fullName"
                    }[1m]
                )
            ) * 60
        """.replace("\\s".toRegex(), "")
        return response
    }

    fun makeGetLatencyPerMinuteQuery(deploy: Deploy, containerEnvironment: ContainerEnvironment, percent: Double): String {
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)

        return """
            histogram_quantile(
                $percent, 
                sum(
                    rate(
                        http_server_duration_milliseconds_bucket {
                            exported_job="$fullName"
                        }[1m]
                    )
                ) by (le)
            )
        """.replace("\\s".toRegex(), "")
    }

    fun makeHttpStatusRequestPerMinuteQuery(deploy: Deploy, containerEnvironment: ContainerEnvironment, statusCode: Int): String {
        val fullName = ContainerUtil.getContainerName(deploy, containerEnvironment)

        return """
            sum(
                rate(
                    http_server_duration_milliseconds_count{
                        exported_job="$fullName",
                        http_status_code="$statusCode"
                    }[1m]
                )
            ) * 60
        """.replace("\\s".toRegex(), "")
    }

    fun formatData(queryResponse: PrometheusDataQueryResponse): MutableMap<String, MutableMap<String, String>> {
        val response = mutableMapOf<String, MutableMap<String, String>>()
        queryResponse.data.result.forEachIndexed { index, data ->
            val metricData = mutableMapOf<String, String>()
            for (value in data.values) {
                if (value.size >= 2) {
                    val timestamp = (value[0] as? Long)?.let { formatTime(it) } ?: continue
                    val metricValue = value[1]?.toString() ?: continue
                    metricData[timestamp] = metricValue
                }
            }
            response[index.toString()] = metricData
        }
        return response
    }

    private fun formatTime(time: Long): String {
        val timestamp = Instant.ofEpochMilli(time)
        val kstTime = ZonedDateTime.ofInstant(timestamp, ZoneId.of("Asia/Seoul"))
        return kstTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    data class LogEntry(val timestamp: String, val body: String)

    fun parseLogs(frames: List<DsDataFrame>?): List<LogEntry> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return frames?.flatMap { frame ->
            frame.data?.let { data ->
                if (data.values.size >= 3) {
                    val timestamps = (data.values[1] as List<Long>).reversed()
                    val bodies = (data.values[2] as List<String>).reversed()
                    timestamps.zip(bodies) { timestamp, body ->
                        val kstTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Seoul"))
                        LogEntry(kstTime.format(formatter), body)
                    }
                } else {
                    emptyList()
                }
            } ?: emptyList()
        }?.sortedBy { it.timestamp } ?: emptyList()
    }
}
