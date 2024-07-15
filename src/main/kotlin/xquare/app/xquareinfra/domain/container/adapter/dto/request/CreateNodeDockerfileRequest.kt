package xquare.app.xquareinfra.domain.container.adapter.dto.request

data class CreateNodeDockerfileRequest(
    val nodeVersion: String,
    val buildCommands: List<String> = emptyList(),
    val builder: String = "node",
    val command: String,
    var port: Int? = null
)