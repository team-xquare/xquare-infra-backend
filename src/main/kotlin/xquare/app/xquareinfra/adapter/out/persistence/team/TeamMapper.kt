package xquare.app.xquareinfra.adapter.out.persistence.team

import org.mapstruct.Mapper
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity

@Mapper(componentModel = "spring")
interface TeamMapper {
    fun toModel(entity: TeamJpaEntity): Team
    fun toEntity(model: Team): TeamJpaEntity

    fun toUserTeamModel(entity: UserTeamJpaEntity): UserTeam
    fun toUserTeamEntity(model: UserTeam): UserTeamJpaEntity
}