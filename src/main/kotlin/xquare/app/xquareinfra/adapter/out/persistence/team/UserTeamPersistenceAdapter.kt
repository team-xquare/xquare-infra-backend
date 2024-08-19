package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.UserTeamRepository
import xquare.app.xquareinfra.adapter.out.persistence.user.UserMapper
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: UserTeamRepository,
    private val teamMapper: TeamMapper,
    private val userMapper: UserMapper
): ExistsUserTeamPort, FindUserTeamPort {
    override fun existsByTeamAndUser(team: Team, user: User): Boolean {
        return userTeamRepository.existsByTeamAndUser(teamMapper.toEntity(team), userMapper.toEntity(user))
    }

    override fun findByUserAndTeam(user: User, team: Team): UserTeam? {
        return userTeamRepository.findByTeamAndUser(teamMapper.toEntity(team), userMapper.toEntity(user))?.let { teamMapper.toUserTeamModel(it) }
    }

}