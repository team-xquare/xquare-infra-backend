package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.infrastructure.external.github.client.dto.request.DockerfileRequest

data class CreateNodeDockerfileRequest(
    val nodeVersion: String,
    val buildCommands: List<String> = emptyList(),
    override val builder: String = "node",
    val command: String,
    var port: Int? = null
) : DockerfileRequest