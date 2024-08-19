package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.UserTeamRepository
import xquare.app.xquareinfra.adapter.out.persistence.user.UserMapper
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: UserTeamRepository,
    private val teamMapper: TeamMapper,
    private val userMapper: UserMapper
): ExistsUserTeamPort, FindUserTeamPort {
    override fun existsByTeamAndUser(team: Team, user: User): Boolean {
        return userTeamRepository.existsByTeamAndUser(teamMapper.toEntity(team), userMapper.toEntity(user))
    }


    override fun findByUserAndTeam(user: UserJpaEntity, teamJpaEntity: TeamJpaEntity): UserTeamJpaEntity? =
        userTeamRepository.findByTeamAndUser(teamJpaEntity, user)
}