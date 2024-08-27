package xquare.app.xquareinfra.infrastructure.persistence.span

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import xquare.app.xquareinfra.domain.span.model.AttributeValue
import xquare.app.xquareinfra.domain.span.model.SpanEvent
import xquare.app.xquareinfra.domain.span.model.SpanLink
import xquare.app.xquareinfra.domain.span.model.SpanStatus

@Document(collection = "spans")
data class SpanMongoEntity(
    @Id
    val id: String,
    val traceId: String,
    val spanId: String,
    val parentSpanId: String?,
    val name: String,
    val kind: Int,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
    val attributes: Map<String, AttributeValue>,
    val events: List<SpanEvent>,
    val links: List<SpanLink>,
    val status: SpanStatus,
    val rootServiceName: String?
)