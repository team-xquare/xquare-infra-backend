package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DockerfileRequest

data class CreateGradleDockerfileRequest(
    val jdkVersion: Int,
    val outputDir: String,
    override val builder: String = "gradle",
    override var buildDir: String = "/",
    val buildCommands: List<String> = listOf()
) : DockerfileRequest
