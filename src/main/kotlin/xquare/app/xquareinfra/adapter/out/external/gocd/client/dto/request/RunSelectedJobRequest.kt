package xquare.app.xquareinfra.adapter.out.external.gocd.client.dto.request

data class RunSelectedJobRequest(
    val jobs: List<String>
)
