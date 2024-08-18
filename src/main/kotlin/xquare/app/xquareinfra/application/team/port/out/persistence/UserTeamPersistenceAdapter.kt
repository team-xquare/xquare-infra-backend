package xquare.app.xquareinfra.application.team.port.out.persistence

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.persistence.repository.UserTeamRepository
import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.UserTeam
import xquare.app.xquareinfra.domain.user.domain.User

@Component
class UserTeamPersistenceAdapter(
    private val userTeamRepository: xquare.app.xquareinfra.application.team.port.out.persistence.repository.UserTeamRepository
): xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort {
    override fun existsByTeamAndUser(team: Team, user: User): Boolean =
        userTeamRepository.existsByTeamAndUser(team, user)

    override fun findByUserAndTeam(user: User, team: Team): UserTeam? =
        userTeamRepository.findByTeamAndUser(team, user)
}