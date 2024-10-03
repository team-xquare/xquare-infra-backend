package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest

data class CreateNodeWithNginxDockerfileRequest(
    val nodeVersion: Int,
    val buildCommands: List<String> = emptyList(),
    override val builder: String = "node_with_nginx",
    override var buildDir: String = "/",
    val outputDir: String,
    var port: Int?
): DockerfileRequest
