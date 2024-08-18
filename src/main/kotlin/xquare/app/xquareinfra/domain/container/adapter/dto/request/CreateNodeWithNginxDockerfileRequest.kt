package xquare.app.xquareinfra.domain.container.adapter.dto.request

import xquare.app.xquareinfra.infrastructure.external.github.client.dto.request.DockerfileRequest

data class CreateNodeWithNginxDockerfileRequest(
    val nodeVersion: Int,
    val buildCommands: List<String> = emptyList(),
    override val builder: String = "node_with_nginx",
    val outputDir: String,
    var port: Int?
): DockerfileRequest
