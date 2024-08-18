package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.team.adapter.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.application.team.port.`in`.CreateTeamUseCase
import xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.SaveTeamPort
import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Transactional
@Service
class CreateTeamService(
    private val saveTeamPort: xquare.app.xquareinfra.application.team.port.out.SaveTeamPort,
    private val existsTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findUserPort: FindUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
): xquare.app.xquareinfra.application.team.port.`in`.CreateTeamUseCase {
    override fun create(req: CreateTeamRequest) {
        if(existsTeamPort.existsByTeamNameEn(req.teamNameEn)) {
            throw BusinessLogicException.ALREADY_EXISTS_TEAM
        }

        val user = readCurrentUserPort.readCurrentUser()

        val team = saveTeamPort.save(
            Team(
                id = null,
                teamNameEn = req.teamNameEn,
                teamNameKo = req.teamNameKo,
                teamType = req.teamType,
                adminId = user.id!!
            )
        )
        user.addTeam(team, TeamMemberRole.ADMINISTRATOR)
        req.teamMemberList.map {
            val addMember = findUserPort.findById(it) ?: throw BusinessLogicException.USER_NOT_FOUND
            if(existsUserTeamPort.existsByTeamAndUser(team, addMember)) {
                throw BusinessLogicException.ALREADY_EXISTS_USER_TEAM
            }
            addMember.addTeam(team, TeamMemberRole.MEMBER)
        }
    }
}