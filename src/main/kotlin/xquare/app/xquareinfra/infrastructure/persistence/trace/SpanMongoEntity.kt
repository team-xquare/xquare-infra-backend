package xquare.app.xquareinfra.infrastructure.persistence.trace

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import xquare.app.xquareinfra.domain.trace.model.SpanEvent
import xquare.app.xquareinfra.domain.trace.model.SpanLink
import xquare.app.xquareinfra.domain.trace.model.SpanStatus
import javax.persistence.Id

@Document(collection = "spans")
data class SpanMongoEntity(
    @Id
    @Field("id")
    val id: String,

    @Indexed
    @Field("traceId")
    val traceId: String,

    @Indexed
    @Field("spanId")
    val spanId: String,

    @Indexed
    @Field("parentSpanId")
    val parentSpanId: String?,

    @Field("name")
    val name: String,

    @Field("kind")
    val kind: Int,

    @Field("startTimeUnixNano")
    val startTimeUnixNano: Long,

    @Field("endTimeUnixNano")
    val endTimeUnixNano: Long,

    @Field("attributes")
    val attributes: Map<String, Any?> = emptyMap(),

    @Field("events")
    val events: List<SpanEvent> = emptyList(),

    @Field("links")
    val links: List<SpanLink> = emptyList(),

    @Field("status")
    val status: SpanStatus,

    @Field("serviceName")
    val serviceName: String?
)