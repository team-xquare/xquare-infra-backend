package xquare.app.xquareinfra.domain.team.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.team.adapter.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.domain.team.application.port.`in`.DeleteTeamMemberUseCase
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindUserTeamPort
import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Transactional
@Service
class DeleteTeamMemberService(
    private val findTeamPort: FindTeamPort,
    private val findUserPort: FindUserPort,
    private val findUserTeamPort: FindUserTeamPort
): DeleteTeamMemberUseCase {
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