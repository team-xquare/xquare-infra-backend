package xquare.app.xquareinfra.domain.container.adapter.dto.request

data class CreateNodeWithNginxDockerfileRequest(
    val nodeVersion: Int,
    val commands: List<String> = emptyList(),
    val builder: String = "node_with_nginx",
    val outputDir: String,
    var port: Int?
)
