package xquare.app.xquareinfra.domain.trace.model

data class CallDetail(
    val callId: String,
    val timestamp: String,
    val status: CallStatus,
    val latencyMs: Double
)
