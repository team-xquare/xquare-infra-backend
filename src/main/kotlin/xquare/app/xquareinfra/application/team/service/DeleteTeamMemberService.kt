package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class DeleteTeamMemberService(
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val findUserPort: FindUserPort,
    private val findUserTeamPort: xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
): xquare.app.xquareinfra.application.team.port.`in`.DeleteTeamMemberUseCase {
    override fun deleteTeamMember(req: DeleteTeamMemberRequest, teamId: UUID) {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val user = findUserPort.findById(req.userId) ?: throw BusinessLogicException.USER_NOT_FOUND

        val userTeam = findUserTeamPort.findByUserAndTeam(user, team) ?: throw BusinessLogicException.USER_TEAM_NOT_FOUND

        if(userTeam.teamMemberRole == TeamMemberRole.ADMINISTRATOR) {
            throw BusinessLogicException.USER_TEAM_BAD_REQUEST
        }
        user.deleteTeam(userTeam)
    }
}