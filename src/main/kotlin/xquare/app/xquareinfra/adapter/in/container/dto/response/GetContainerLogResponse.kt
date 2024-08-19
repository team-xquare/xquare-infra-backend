package xquare.app.xquareinfra.adapter.`in`.container.dto.response

data class LogEntry(
    val timestamp: String,
    val body: String
)

data class GetContainerLogResponse(
    val logs: List<Any?>
) {
    override fun toString(): String = logs.joinToString("\n") { it.toString() }
}
