package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.application.team.port.`in`.CreateTeamUseCase
import xquare.app.xquareinfra.application.team.port.out.ExistsTeamPort
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.SaveTeamPort
import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Transactional
@Service
class CreateTeamService(
    private val saveTeamPort: SaveTeamPort,
    private val existsTeamPort: ExistsTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findUserPort: FindUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort
) : CreateTeamUseCase {
    override fun create(req: CreateTeamRequest) {
        if (existsTeamPort.existsByTeamNameEn(req.teamNameEn)) {
            throw BusinessLogicException.ALREADY_EXISTS_TEAM
        }

        val currentUser = readCurrentUserPort.readCurrentUser()

        val team = Team(
            id = null,
            teamNameEn = req.teamNameEn,
            teamNameKo = req.teamNameKo,
            teamType = req.teamType,
            adminId = currentUser.id!!
        )

        val userTeams = mutableListOf<UserTeam>()

        // 현재 사용자를 관리자로 추가
        userTeams.add(UserTeam(
            id = null,
            user = currentUser,
            team = team,
            role = TeamMemberRole.ADMINISTRATOR
        ))

        // 팀 멤버 추가
        req.teamMemberList.forEach { memberId ->
            val member = findUserPort.findById(memberId) ?: throw BusinessLogicException.USER_NOT_FOUND
            if (existsUserTeamPort.existsByTeamAndUser(team, member)) {
                throw BusinessLogicException.ALREADY_EXISTS_USER_TEAM
            }
            userTeams.add(UserTeam(
                id = null,
                user = member,
                team = team,
                role = TeamMemberRole.MEMBER
            ))
        }

        // 팀과 UserTeam 관계를 함께 저장
        saveTeamPort.saveTeamWithMembers(team, userTeams)
    }
}