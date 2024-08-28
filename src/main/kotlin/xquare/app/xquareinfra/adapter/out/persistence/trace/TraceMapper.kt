package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

@Mapper(componentModel = "spring")
interface TraceMapper {
    fun toModel(entity: TraceMongoEntity): Trace
    fun toEntity(model: Trace): TraceMongoEntity
}