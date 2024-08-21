package xquare.app.xquareinfra.adapter.out.persistence.team

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.team.repository.UserTeamRepository
import xquare.app.xquareinfra.adapter.out.persistence.user.UserMapper
import xquare.app.xquareinfra.application.team.port.out.*
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.user.model.User
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: UserTeamRepository,
    private val teamMapper: TeamMapper,
    private val userMapper: UserMapper,
    private val findTeamPort: FindTeamPort
): ExistsUserTeamPort, FindUserTeamPort, SaveUserTeamPort, DeleteUserTeamPort {
    override fun existsByTeamIdAndUser(teamId: UUID, user: User): Boolean {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        return userTeamRepository.existsByTeamAndUser(teamMapper.toEntity(team), userMapper.toEntity(user))
    }

    override fun findByUserAndTeam(user: User, team: Team): UserTeam? {
        return userTeamRepository.findByTeamAndUser(teamMapper.toEntity(team), userMapper.toEntity(user))?.let { teamMapper.toUserTeamModel(it) }
    }

    override fun findAllByUser(user: User): List<UserTeam> {
        return userTeamRepository.findAllByUser(userMapper.toEntity(user)).map { teamMapper.toUserTeamModel(it) }
    }

    override fun findAllByTeam(team: Team): List<UserTeam> {
        return userTeamRepository.findAllByTeam(teamMapper.toEntity(team)).map { teamMapper.toUserTeamModel(it) }
    }

    override fun saveUserTeam(userTeam: UserTeam): UserTeam {
        return teamMapper.toUserTeamModel(userTeamRepository.save(teamMapper.toUserTeamEntity(userTeam)))
    }

    override fun deleteByUserAndTeam(user: User, team: Team) {
        userTeamRepository.deleteByUserAndTeam(userMapper.toEntity(user), teamMapper.toEntity(team))
    }
}