package xquare.app.xquareinfra.domain.trace.model

data class Edge(
    val source: String,
    val target: String,
    val interactionType: InteractionType,
    var calls: Int = 0,
    var successes: Int = 0,
    var failures: Int = 0,
    var latencySumMs: Double = 0.0,
    var latencyAvgMs: Double = 0.0,
    val details: MutableList<CallDetail> = mutableListOf()
)