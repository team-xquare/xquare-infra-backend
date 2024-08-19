package xquare.app.xquareinfra.adapter.out.persistence.user

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toModel(entity: UserJpaEntity): User
    fun toEntity(model: User): UserJpaEntity

    fun toUserTeamModel(entity: UserTeamJpaEntity): UserTeam
    fun toUserTeamEntity(model: UserTeam): UserTeamJpaEntity
}