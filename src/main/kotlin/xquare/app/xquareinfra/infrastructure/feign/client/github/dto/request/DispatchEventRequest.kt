package xquare.app.xquareinfra.infrastructure.feign.client.github.dto.request

data class DispatchEventRequest(
    val event_type: String,
    val client_payload: Map<String, Any>
)
