package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.domain.model.domain.role.TeamMemberRole
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.UUID

@Transactional
@Service
class AddUserTeamService(
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val findUserPort: FindUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort
): xquare.app.xquareinfra.application.team.port.`in`.AddTeamMemberUseCase {
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