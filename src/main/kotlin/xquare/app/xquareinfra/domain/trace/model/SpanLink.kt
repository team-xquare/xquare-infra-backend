package xquare.app.xquareinfra.domain.trace.model

import org.springframework.data.mongodb.core.mapping.Field

data class SpanLink(
    @Field("traceId")
    val traceId: String,

    @Field("spanId")
    val spanId: String,

    @Field("attributes")
    val attributes: Map<String, Any>
)