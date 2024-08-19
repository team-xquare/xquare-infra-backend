package xquare.app.xquareinfra.adapter.out.persistence.deploy

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

@Mapper(componentModel = "spring")
interface DeployMapper {
    fun toModel(entity: DeployJpaEntity): Deploy
    fun toEntity(model: Deploy): DeployJpaEntity
}