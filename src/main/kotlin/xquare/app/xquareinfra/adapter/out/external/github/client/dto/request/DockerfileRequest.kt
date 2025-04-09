package xquare.app.xquareinfra.adapter.out.external.github.client.dto.request

interface DockerfileRequest {
    val builder: String
    var buildDir: String
}