package xquare.app.xquareinfra.domain.trace.model

import org.springframework.data.mongodb.core.mapping.Field

data class SpanEvent(
    @Field("timeUnixNano")
    val timeUnixNano: Long,

    @Field("name")
    val name: String,

    @Field("attributes")
    val attributes: Map<String, Any>
)
