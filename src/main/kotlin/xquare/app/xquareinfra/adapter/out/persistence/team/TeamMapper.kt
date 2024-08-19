package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.Mapper
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity

@Component
class TeamMapper(
    private val findUserPort: FindUserPort,
    private val findUserTeamPort: FindUserTeamPort
) : Mapper<Team, TeamJpaEntity> {
    override fun toEntity(domain: Team): TeamJpaEntity {
        return TeamJpaEntity(
            id = domain.id,
            adminId = domain.adminId,
            teamNameKo = domain.teamNameKo,
            teamNameEn = domain.teamNameEn,
            teamType = domain.teamType
        )
    }

    override fun toDomain(entity: TeamJpaEntity): Team {
        val members = entity.members.mapNotNull { userTeamJpaEntity ->
            val user = findUserPort.findById(userTeamJpaEntity.userJpaEntity.id!!)
            user?.let {
                val userTeam = findUserTeamPort.findByUserAndTeam(user, entity)
                userTeam?.let {
                    UserTeam(
                        id = it.id,
                        userId = user.id!!,
                        teamId = entity.id!!,
                        role = it.teamMemberRole
                    )
                }
            }
        }.toSet()
        return Team(
            id = entity.id,
            adminId = entity.adminId,
            teamNameKo = entity.teamNameKo,
            teamNameEn = entity.teamNameEn,
            teamType = entity.teamType,
            members = members,
            createdAt = entity.createdAt!!
        )
    }
}