package xquare.app.xquareinfra.domain.team.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.team.adapter.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.domain.team.application.port.`in`.AddTeamMemberUseCase
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.UUID

@Transactional
@Service
class AddUserTeamService(
    private val findTeamPort: FindTeamPort,
    private val findUserPort: FindUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort
): AddTeamMemberUseCase{
    override fun addTeamMember(req: AddTeamMemberRequest, teamId: UUID) {
        val user = readCurrentUserPort.readCurrentUser()
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND

        if(user.id != team.adminId) {
            throw BusinessLogicException.ADD_TEAM_PERMISSION_DENIED
        }

        req.members.map {
            val addMember = findUserPort.findById(it) ?: throw BusinessLogicException.USER_NOT_FOUND
            if(existsUserTeamPort.existsByTeamAndUser(team, addMember)) {
                throw BusinessLogicException.ALREADY_EXISTS_USER_TEAM
            }
            addMember.addTeam(team, TeamMemberRole.MEMBER)
        }
    }
}