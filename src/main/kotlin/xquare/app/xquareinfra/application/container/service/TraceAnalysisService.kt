package xquare.app.xquareinfra.application.container.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@Service
class TraceAnalysisService {
    fun analyzeHttpRequestsPerMinute(traces: List<Trace>): Map<String, String> {
        val requestsPerMinute = mutableMapOf<String, Int>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = Instant.ofEpochSecond(0, trace.dateNano)
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()

            requestsPerMinute[minuteKey] = requestsPerMinute.getOrDefault(minuteKey, 0) + 1
        }

        return requestsPerMinute.mapValues { (_, count) -> count.toString() }
    }

    fun analyzeHttpStatusCodes(traces: List<Trace>, statusCode: Int): Map<String, String> {
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

        return statusCodeCountsPerMinute.mapValues { (_, count) -> count.toString() }
    }

    fun analyzeLatency(traces: List<Trace>, percentile: Int): Map<String, String> {
        val latenciesPerMinute = mutableMapOf<String, MutableList<Long>>()

        traces.forEach { trace ->
            val rootSpan = trace.getRootSpan() ?: return@forEach
            if (!rootSpan.isHttpRequest()) return@forEach

            val minuteKey = Instant.ofEpochSecond(0, trace.dateNano)
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()

            latenciesPerMinute.getOrPut(minuteKey) { mutableListOf() }.add(trace.durationNano)
        }

        return latenciesPerMinute.mapValues { (_, latencies) ->
            if (latencies.isEmpty()) {
                "0.00"
            } else {
                val sortedLatencies = latencies.sorted()
                val index = ceil(sortedLatencies.size * (percentile / 100.0)).toInt() - 1
                val percentileLatency = sortedLatencies[index.coerceIn(0, sortedLatencies.lastIndex)]
                val percentileLatencyMs = TimeUtil.unixNanoToMilliseconds(percentileLatency)
                String.format("%.2f", percentileLatencyMs)
            }
        }
    }
}