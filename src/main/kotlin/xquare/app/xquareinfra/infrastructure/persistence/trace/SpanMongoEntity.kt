package xquare.app.xquareinfra.infrastructure.persistence.trace

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import xquare.app.xquareinfra.domain.trace.model.AttributeValue
import xquare.app.xquareinfra.domain.trace.model.SpanEvent
import xquare.app.xquareinfra.domain.trace.model.SpanLink
import xquare.app.xquareinfra.domain.trace.model.SpanStatus

@CompoundIndex(def = "{'attributes.service.name': 1}")
@Document(collection = "spans")
data class SpanMongoEntity(
    @Id
    val id: String,
    val traceId: String,
    val spanId: String,
    @Indexed
    val parentSpanId: String?,
    val name: String,
    val kind: Int,
    @Indexed
    val startTimeUnixNano: Long,
    @Indexed
    val endTimeUnixNano: Long,
    val attributes: Map<String, AttributeValue>,
    val events: List<SpanEvent>,
    val links: List<SpanLink>,
    val status: SpanStatus
)