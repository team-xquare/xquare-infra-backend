package xquare.app.xquareinfra.infrastructure.external.feign.client.gocd.dto.request

data class RunSelectedJobRequest(
    val jobs: List<String>
)
