package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.UserTeamRepository
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: UserTeamRepository
): ExistsUserTeamPort, FindUserTeamPort {
    override fun existsByTeamAndUser(teamJpaEntity: TeamJpaEntity, userJpaEntity: UserJpaEntity): Boolean =
        userTeamRepository.existsByTeamAndUser(teamJpaEntity, userJpaEntity)

    override fun findByUserAndTeam(userJpaEntity: UserJpaEntity, teamJpaEntity: TeamJpaEntity): UserTeamJpaEntity? =
        userTeamRepository.findByTeamAndUser(teamJpaEntity, userJpaEntity)
}