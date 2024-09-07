package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@Service
class TraceAnalysisService {
    fun analyzeHttpRequestsPerMinute(traces: List<Trace>, startTimeNano: Long, endTimeNano: Long): Map<String, String> {
        val requestsPerMinute = mutableMapOf<String, Int>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = getMinuteKey(trace.dateNano)

            requestsPerMinute[minuteKey] = requestsPerMinute.getOrDefault(minuteKey, 0) + 1
        }

        return generateMinuteKeys(startTimeNano, endTimeNano).associateWith { key ->
            requestsPerMinute[key]?.toString() ?: "0"
        }
    }

    fun analyzeHttpStatusCodes(traces: List<Trace>, statusCode: Int, startTimeNano: Long, endTimeNano: Long): Map<String, String> {
        val statusCodeCountsPerMinute = mutableMapOf<String, Int>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            if (rootSpan.getStatusCode() == statusCode) {
                val minuteKey = getMinuteKey(trace.dateNano)

                statusCodeCountsPerMinute[minuteKey] = statusCodeCountsPerMinute.getOrDefault(minuteKey, 0) + 1
            }
        }

        return generateMinuteKeys(startTimeNano, endTimeNano).associateWith { key ->
            statusCodeCountsPerMinute[key]?.toString() ?: "0"
        }
    }

    fun analyzeLatency(traces: List<Trace>, percentile: Int, startTimeNano: Long, endTimeNano: Long): Map<String, String> {
        val latenciesPerMinute = mutableMapOf<String, MutableList<Long>>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = getMinuteKey(trace.dateNano)

            latenciesPerMinute.getOrPut(minuteKey) { mutableListOf() }.add(trace.durationNano)
        }

        return generateMinuteKeys(startTimeNano, endTimeNano).associateWith { key ->
            latenciesPerMinute[key]?.let { latencies ->
                if (latencies.isEmpty()) {
                    "0"
                } else {
                    val sortedLatencies = latencies.sorted()
                    val index = ceil(sortedLatencies.size * (percentile / 100.0)).toInt() - 1
                    val percentileLatency = sortedLatencies[index.coerceIn(0, sortedLatencies.lastIndex)]
                    val percentileLatencyMs = TimeUtil.unixNanoToMilliseconds(percentileLatency)
                    percentileLatencyMs.toString()
                }
            } ?: "0"
        }
    }

    private fun getMinuteKey(timeNano: Long): String {
        return Instant.ofEpochSecond(0, timeNano)
            .truncatedTo(ChronoUnit.MINUTES)
            .toString()
    }

    private fun generateMinuteKeys(startTimeNano: Long, endTimeNano: Long): List<String> {
        val keys = mutableListOf<String>()
        var currentTimeNano = startTimeNano - (startTimeNano % 60_000_000_000)  // Round down to the nearest minute
        while (currentTimeNano <= endTimeNano) {
            keys.add(getMinuteKey(currentTimeNano))
            currentTimeNano += 60_000_000_000  // Add one minute in nanoseconds
        }
        return keys
    }
}