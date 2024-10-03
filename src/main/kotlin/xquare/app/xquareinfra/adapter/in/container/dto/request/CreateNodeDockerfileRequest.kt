package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest

data class CreateNodeDockerfileRequest(
    val nodeVersion: String,
    val buildCommands: List<String> = emptyList(),
    override val builder: String = "node",
    override var buildDir: String = "/",
    val command: String,
    var port: Int? = null
) : DockerfileRequest