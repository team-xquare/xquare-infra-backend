package xquare.app.xquareinfra.domain.container.adapter.dto.request

data class CreateGradleDockerfileRequest(
    val jdkVersion: Int,
    val outputDir: String,
    val builder: String = "gradle",
    val commands: List<String> = listOf(),
)
