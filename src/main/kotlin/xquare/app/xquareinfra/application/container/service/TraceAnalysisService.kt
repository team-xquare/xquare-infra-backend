package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@Service
class TraceAnalysisService {
    fun analyzeHttpRequestsPerMinute(traces: List<Trace>, startTime: Instant, endTime: Instant): Map<String, String> {
        val requestsPerMinute = mutableMapOf<String, Int>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = Instant.ofEpochSecond(0, trace.dateNano)
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()

            requestsPerMinute[minuteKey] = requestsPerMinute.getOrDefault(minuteKey, 0) + 1
        }

        return generateMinuteKeys(startTime, endTime).associateWith { key ->
            requestsPerMinute[key]?.toString() ?: "0"
        }
    }

    fun analyzeHttpStatusCodes(traces: List<Trace>, statusCode: Int, startTime: Instant, endTime: Instant): Map<String, String> {
        val statusCodeCountsPerMinute = mutableMapOf<String, Int>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            if (rootSpan.getStatusCode() == statusCode) {
                val minuteKey = Instant.ofEpochSecond(0, trace.dateNano)
                    .truncatedTo(ChronoUnit.MINUTES)
                    .toString()

                statusCodeCountsPerMinute[minuteKey] = statusCodeCountsPerMinute.getOrDefault(minuteKey, 0) + 1
            }
        }

        return generateMinuteKeys(startTime, endTime).associateWith { key ->
            statusCodeCountsPerMinute[key]?.toString() ?: "0"
        }
    }

    fun analyzeLatency(traces: List<Trace>, percentile: Int, startTime: Instant, endTime: Instant): Map<String, String> {
        val latenciesPerMinute = mutableMapOf<String, MutableList<Long>>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = Instant.ofEpochSecond(0, trace.dateNano)
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()

            latenciesPerMinute.getOrPut(minuteKey) { mutableListOf() }.add(trace.durationNano)
        }

        return generateMinuteKeys(startTime, endTime).associateWith { key ->
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

    private fun generateMinuteKeys(startTime: Instant, endTime: Instant): List<String> {
        val keys = mutableListOf<String>()
        var currentTime = startTime.truncatedTo(ChronoUnit.MINUTES)
        while (currentTime.isBefore(endTime) || currentTime == endTime) {
            keys.add(currentTime.toString())
            currentTime = currentTime.plus(1, ChronoUnit.MINUTES)
        }
        return keys
    }
}