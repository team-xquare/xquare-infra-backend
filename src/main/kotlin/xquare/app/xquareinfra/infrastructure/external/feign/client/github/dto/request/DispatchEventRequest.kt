package xquare.app.xquareinfra.infrastructure.external.client.github.dto.request

data class DispatchEventRequest(
    val event_type: String,
    val client_payload: Map<String, Any>
)
