package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.UserTeamRepository
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.model.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: UserTeamRepository
): xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort {
    override fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, user: User): Boolean =
        userTeamRepository.existsByTeamAndUser(teamJpaEntity, user)

    override fun findByUserAndTeam(user: User, teamJpaEntity: TeamJpaEntity): UserTeam? =
        userTeamRepository.findByTeamAndUser(teamJpaEntity, user)
}