package xquare.app.xquareinfra.domain.team.application.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.domain.team.adapter.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.domain.team.application.port.`in`.CreateTeamUseCase
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.SaveTeamMemberPort
import xquare.app.xquareinfra.domain.team.application.port.out.SaveTeamPort
import xquare.app.xquareinfra.domain.team.domain.Team
import xquare.app.xquareinfra.domain.team.domain.TeamMember
import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.application.port.out.ExistsUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Service
class CreateTeamService(
    private val saveTeamPort: SaveTeamPort,
    private val saveTeamMemberPort: SaveTeamMemberPort,
    private val existsTeamPort: ExistsTeamPort,
    private val existsUserPort: ExistsUserPort
): CreateTeamUseCase {
    override fun create(req: CreateTeamRequest) {
        if(existsTeamPort.existsByTeamNameEn(req.teamNameEn)) {
            throw BusinessLogicException.ALREADY_EXISTS_TEAM
        }
        val team = Team(
            id = null,
            teamNameEn = req.teamNameEn,
            teamNameKo = req.teamNameKo,
            teamType = req.teamType
        )

        saveTeamPort.save(team)

        req.teamMemberList.map {
            if(!existsUserPort.existsById(it)) {
                throw BusinessLogicException.USER_NOT_FOUND
            }
            saveTeamMemberPort.save(
                TeamMember(
                    id = null,
                    memberId = it,
                    teamMemberRole = TeamMemberRole.MEMBER,
                    team = team
                )
            )
        }
    }
}