package xquare.app.xquareinfra.application.trace.service

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.*
import xquare.app.xquareinfra.application.trace.interaction.InteractionHandlerFactory
import xquare.app.xquareinfra.domain.trace.model.*
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.time.Instant

@Component
class ServiceMapBuilder(
    private val handlerFactory: InteractionHandlerFactory
) {

    fun buildServiceMap(spans: List<Span>): ServiceMap {
        val nodesMap: MutableMap<String, Node> = mutableMapOf()
        val edgesMap: MutableMap<Pair<String, String>, Edge> = mutableMapOf()

        val traceMap = groupSpansByTrace(spans)

        traceMap.forEach { (_, traceSpans) ->
            val spanDict = traceSpans.associateBy { it.spanId }
            traceSpans.forEach { span ->
                span.parentSpanId?.let { parentId ->
                    spanDict[parentId]?.let { parentSpan ->
                        val handler = handlerFactory.getHandler(span)
                        val interactionType = handler.extractInteractionType(span)
                        val status = handler.extractStatus(span)

                        val edgeKey = parentSpan.serviceName to span.serviceName
                        val edge = edgesMap.getOrDefault(edgeKey, Edge(
                            source = parentSpan.serviceName,
                            target = span.serviceName,
                            interactionType = interactionType
                        ))

                        edge.calls += 1
                        when (status) {
                            CallStatus.UNKNOWN -> {}
                            CallStatus.FAILURE -> edge.failures += 1
                            CallStatus.SUCCESS -> edge.successes += 1
                        }

                        val latencyMs = TimeUtil.unixNanoToMilliseconds(span.endTimeUnixNano - span.startTimeUnixNano).toDouble()
                        edge.latencySumMs += latencyMs

                        val callId = "${span.traceId}_${span.spanId}"
                        val timestamp = TimeUtil.unixNanoToKoreanTime(span.startTimeUnixNano)
                        val callDetail = CallDetail(
                            callId = callId,
                            timestamp = timestamp,
                            status = status,
                            latencyMs = latencyMs
                        )
                        edge.details.add(callDetail)

                        edgesMap[edgeKey] = edge
                    }
                }
            }
        }

        spans.forEach { span ->
            val node = nodesMap.getOrDefault(span.serviceName, Node(
                nodeId = span.serviceName,
                type = determineNodeType(span)
            ))

            node.calls += 1
            val handler = handlerFactory.getHandler(span)
            val status = handler.extractStatus(span)
            when (status) {
                CallStatus.UNKNOWN -> {}
                CallStatus.FAILURE -> node.failures += 1
                CallStatus.SUCCESS -> node.successes += 1
            }
            val latencyMs = TimeUtil.unixNanoToMilliseconds(span.endTimeUnixNano - span.startTimeUnixNano).toDouble()
            node.latencySumMs += latencyMs

            nodesMap[span.serviceName] = node
        }

        nodesMap.values.forEach { node ->
            node.latencyAvgMs = if (node.calls > 0) node.latencySumMs / node.calls else 0.0
        }

        edgesMap.values.forEach { edge ->
            edge.latencyAvgMs = if (edge.calls > 0) edge.latencySumMs / edge.calls else 0.0
        }

        return ServiceMap(
            edges = edgesMap.values.toList(),
            nodes = nodesMap.values.toList()
        )
    }

    private fun groupSpansByTrace(spans: List<Span>): Map<String, List<Span>> {
        return spans.groupBy { it.traceId }
    }

    private fun determineNodeType(span: Span): NodeType {
        val handler = handlerFactory.getHandler(span)
        return when (handler.extractInteractionType(span)) {
            InteractionType.DB -> NodeType.DATABASE
            InteractionType.KAFKA -> NodeType.MESSAGING
            else -> NodeType.SERVICE
        }
    }

    fun toServiceMapResponse(spans: List<Span>): ServiceMapResponse {
        val serviceMap = buildServiceMap(spans)

        val nodeDTOs = serviceMap.nodes.map { node ->
            NodeDTO(
                nodeId = node.nodeId,
                type = node.type,
                calls = node.calls,
                successes = node.successes,
                failures = node.failures,
                latencyAvgMs = node.latencyAvgMs
            )
        }

        val edgeDTOs = serviceMap.edges.map { edge ->
            EdgeDTO(
                source = edge.source,
                target = edge.target,
                interactionType = edge.interactionType,
                calls = edge.calls,
                successes = edge.successes,
                failures = edge.failures,
                latencyAvgMs = edge.latencyAvgMs
            )
        }

        val metric = Metric(
            timestamp = Instant.now().toString(),
            nodes = nodeDTOs,
            edges = edgeDTOs
        )

        return ServiceMapResponse(
            status = "success",
            data = ServiceMapData(
                interval = 60,
                metrics = listOf(metric)
            )
        )
    }
}
