package xquare.app.xquareinfra.adapter.out.persistence.user

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.Mapper
import xquare.app.xquareinfra.application.team.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

@Component
class UserMapper(
    private val findTeamPort: FindTeamPort
) : Mapper<User, UserJpaEntity> {

    override fun toEntity(domain: User): UserJpaEntity {
        val userJpaEntity = UserJpaEntity(
            id = domain.id,
            name = domain.name,
            accountId = domain.accountId,
            grade = domain.grade,
            classNum = domain.classNum,
            number = domain.number,
            roles = domain.roles.toMutableList(),
            email = domain.email
        )

        return userJpaEntity
    }

    override fun toDomain(entity: UserJpaEntity): User {
        val teams = entity.teams.map { userTeamJpaEntity ->
            UserTeam(
                teamId = userTeamJpaEntity.teamJpaEntity.id!!,
                role = userTeamJpaEntity.teamMemberRole,
                id = userTeamJpaEntity.id,
                userId = userTeamJpaEntity.userJpaEntity.id!!
            )
        }.toSet()

        return User(
            id = entity.id,
            name = entity.name,
            accountId = entity.accountId,
            grade = entity.grade,
            classNum = entity.classNum,
            number = entity.number,
            roles = entity.roles.toList(),
            email = entity.email,
            teams = teams
        )
    }

    fun toEntityWithTeams(domain: User): UserJpaEntity {
        val userJpaEntity = toEntity(domain)

        domain.teams.forEach { userTeam ->
            val teamJpaEntity = findTeamPort.findById(userTeam.teamId)
            teamJpaEntity?.let {
                userJpaEntity.addTeam(it, userTeam.role)
            }
        }

        return userJpaEntity
    }
}