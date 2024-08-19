package xquare.app.xquareinfra.adapter.out.persistence.container

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity

@Mapper(componentModel = "spring")
interface ContainerMapper {
    fun toModel(entity: ContainerJpaEntity): Container
    fun toEntity(model: Container): ContainerJpaEntity
}