package xquare.app.xquareinfra.infrastructure.persistence.trace

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.index.Indexed
import xquare.app.xquareinfra.domain.trace.model.Span
import javax.persistence.Id


@Document(collection = "traces")
data class TraceMongoEntity(
    @Id
    @Field("traceId")
    val traceId: String,

    @Field("spans")
    val spans: List<Span>,

    @Field("serviceName")
    @Indexed
    val serviceName: String?,

    @Field("dateNano")
    @Indexed
    val dateNano: Long,

    @Field("durationNano")
    val durationNano: Long
)