package xquare.app.xquareinfra.infrastructure.external.client.data

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.DeployType
import xquare.app.xquareinfra.infrastructure.external.feign.client.data.dto.DataQueryResponse
import xquare.app.xquareinfra.infrastructure.external.feign.client.data.dto.Frame
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

    fun aggregateDataToMinute(rawData: MutableMap<String, Map<String, String>>, intervalSeconds: Int): MutableMap<String, Map<String, String>> {
        val aggregatedData = mutableMapOf<String, MutableMap<String, Double>>()
        val entriesPerMinute = 60 / intervalSeconds

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

    fun getFullName(
        containerName: String,
        serviceType: DeployType,
        envType: ContainerEnvironment,
        isV2: Boolean
    ): String {
        var fullName: String
        if (isV2) {
            fullName = "${containerName}-${envType.toString().lowercase()}"
        } else {
            fullName = "${containerName}-${serviceType.toString().lowercase()}-${envType.toString().lowercase()}"
        }
        return fullName
    }

    fun makeLogQuery(
        team: String,
        containerName: String,
        serviceType: DeployType,
        envType: ContainerEnvironment,
        isV2: Boolean
    ): String {
        val response = "{job=\"$team-${envType.toString().lowercase()}/${getFullName(containerName, serviceType, envType, isV2)}\", container=~\"${getFullName(containerName, serviceType, envType, isV2)}\"} |~ \"(?i)\" \n"
        return response
    }

    fun makeCpuUsageQuery(team: String, containerName: String, serviceType: DeployType, envType: ContainerEnvironment, isV2: Boolean): String {
        val fullName = getFullName(containerName, serviceType, envType, isV2)
        val namespace = "$team-${envType.toString().lowercase()}"

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
        """.trimIndent()
    }

    fun makeMemoryUsageQuery(team: String, containerName: String, serviceType: DeployType, envType: ContainerEnvironment, isV2: Boolean): String {
        val fullName = getFullName(containerName, serviceType, envType, isV2)
        val namespace = "$team-${envType.toString().lowercase()}"

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
        """.trimIndent()
    }

    fun makeRequestPerMinuteQuery(team: String, containerName: String, serviceType: DeployType, envType: ContainerEnvironment, isV2: Boolean): String {
        val fullName = getFullName(containerName, serviceType, envType, isV2)
        val response = """
            sum(
                rate(
                    http_server_duration_milliseconds_count{
                        exported_job="$fullName"
                    }[1m]
                )
            ) * 60
        """.trimIndent()
        println("request per minute query : $response" )
        return response
    }

    fun makeGetLatencyPerMinuteQuery(containerName: String, serviceType: DeployType, envType: ContainerEnvironment, isV2: Boolean, percent: Double): String {
        val fullName = getFullName(containerName, serviceType, envType, isV2)

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
        """.trimIndent()
    }

    fun makeHttpStatus500RequestPerMinuteQuery(containerName: String, serviceType: DeployType, envType: ContainerEnvironment, isV2: Boolean): String {
        val fullName = getFullName(containerName, serviceType, envType, isV2)

        return """
            sum(
                rate(
                    http_server_duration_milliseconds_count{
                        exported_job="$fullName",
                        http_status_code="500"
                    }[1m]
                )
            ) * 60
        """.trimIndent()
    }

    fun formatData(queryResponse: DataQueryResponse): MutableMap<String, Map<String, String>> {
        var count = 0
        val response = mutableMapOf<String, Map<String, String>>()
        queryResponse.results?.a?.frames?.forEach { frame ->
            frame.data?.let { data ->
                if (data.values.isNotEmpty()) {
                    val times = data.values[0]
                    val usages = data.values[1]

                    val timeToUsageMap = mutableMapOf<String, String>()
                    times.zip(usages).forEach { (time, usage) ->
                        val formattedTime = formatTime(time.toString())
                        timeToUsageMap[formattedTime] = usage.toString()
                    }
                    count++
                    response[count.toString()] = timeToUsageMap
                }
            }
        }
        return response
    }

    private fun formatTime(time: String): String {
        val timestamp = Instant.ofEpochMilli(time.toLong())
        val kstTime = ZonedDateTime.ofInstant(timestamp, ZoneId.of("Asia/Seoul"))
        return kstTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    data class LogEntry(val timestamp: String, val body: String)

    fun parseLogs(frames: List<Frame>?): List<LogEntry> {
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
