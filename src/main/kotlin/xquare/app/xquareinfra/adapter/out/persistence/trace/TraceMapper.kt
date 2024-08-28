package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.user.model.User

@Mapper(componentModel = "spring")
interface TraceMapper {
    fun toModel(entity: TraceMongo): User
    fun toEntity(model: User): UserJpaEntityfun t
}