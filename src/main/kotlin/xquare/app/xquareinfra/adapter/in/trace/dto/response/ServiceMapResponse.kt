package xquare.app.xquareinfra.adapter.`in`.trace.dto.response

import xquare.app.xquareinfra.domain.trace.model.InteractionType
import xquare.app.xquareinfra.domain.trace.model.NodeType

data class ServiceMapResponse(
    val data: ServiceMapData
)

data class ServiceMapData(
    val metrics: List<Metric>
)

data class Metric(
    val timestamp: String,
    val nodes: List<NodeDTO>,
    val edges: List<EdgeDTO>
)

data class NodeDTO(
    val nodeId: String,
    val type: NodeType,
    val calls: Int,
    val successes: Int,
    val failures: Int,
    val latencyAvgMs: Double
)

data class EdgeDTO(
    val source: String,
    val target: String,
    val interactionType: InteractionType,
    val calls: Int,
    val successes: Int,
    val failures: Int,
    val latencyAvgMs: Double
)