package xquare.app.xquareinfra.adapter.out.persistence.span

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.infrastructure.persistence.span.SpanMongoEntity

@Mapper(componentModel = "spring")
interface SpanMapper {
    fun toModel(entity: SpanMongoEntity): Span
    fun toEntity(model: Span): SpanMongoEntity
}