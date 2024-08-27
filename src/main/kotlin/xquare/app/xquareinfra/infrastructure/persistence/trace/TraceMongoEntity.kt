package xquare.app.xquareinfra.infrastructure.persistence.trace

import org.springframework.data.mongodb.core.mapping.Document
import xquare.app.xquareinfra.domain.trace.model.Span
import javax.persistence.Id

@Document(collection = "traces")
data class TraceMongoEntity(
    @Id
    val traceId: String,
    val spans: List<Span>,
    val serviceName: String?
)