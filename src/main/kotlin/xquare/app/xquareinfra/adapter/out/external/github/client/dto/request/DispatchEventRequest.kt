package xquare.app.xquareinfra.adapter.out.external.github.client.dto.request

data class DispatchEventRequest(
    val event_type: String,
    val client_payload: Map<String, Any>
)
