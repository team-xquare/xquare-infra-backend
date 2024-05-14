package xquare.app.xquareinfra.domain.container.adapter.dto.response

data class GetContainerLogResponse(
    val logs: List<Any?>
) {
    override fun toString(): String = logs.joinToString("\n") { it.toString() }
}


data class LogEntry(
    val timestamp: Long,
    val body: String
)