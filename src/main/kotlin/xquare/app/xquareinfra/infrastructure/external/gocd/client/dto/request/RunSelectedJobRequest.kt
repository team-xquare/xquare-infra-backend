package xquare.app.xquareinfra.infrastructure.external.gocd.client.dto.request

data class RunSelectedJobRequest(
    val jobs: List<String>
)
