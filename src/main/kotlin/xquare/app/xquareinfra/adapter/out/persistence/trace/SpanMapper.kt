package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.infrastructure.persistence.trace.SpanMongoEntity

@Mapper(componentModel = "spring")
interface SpanMapper {
    fun toModel(entity: SpanMongoEntity): Span
    fun toEntity(model: Span): SpanMongoEntity
}