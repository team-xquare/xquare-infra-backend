package xquare.app.xquareinfra.domain.team.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.application.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.domain.team.adapter.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.domain.team.adapter.dto.response.TeamMemberResponse
import xquare.app.xquareinfra.domain.team.application.port.`in`.GetTeamDetailUseCase
import xquare.app.xquareinfra.domain.team.application.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.domain.team.application.port.out.FindTeamPort
import xquare.app.xquareinfra.domain.team.domain.UserTeam
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Transactional(readOnly = true)
@Service
class GetTeamDetailService(
    private val findTeamPort: FindTeamPort,
    private val findUserPort: FindUserPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort
): GetTeamDetailUseCase {
    override fun getTeamDetail(teamId: UUID): DetailTeamResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val user = readCurrentUserPort.readCurrentUser()

        if(!existsUserTeamPort.existsByTeamAndUser(team, user)) {
            throw XquareException.FORBIDDEN
        }

        val response = team.run {
            val admin = findUserPort.findById(adminId) ?: throw BusinessLogicException.USER_NOT_FOUND
            DetailTeamResponse(
                teamNameEn = teamNameEn,
                teamNameKo = teamNameKo,
                memberCount = members.size,
                adminName = admin.name,
                createdAt = createdAt!!,
                memberList = getMemberResponse(members),
                isAdmin = team.adminId == user.id!!
            )
        }
        return response
    }

    private fun getMemberResponse(members: MutableSet<UserTeam>): List<TeamMemberResponse> {
        return members.map {
            val member = it.user
            TeamMemberResponse(
                memberName = member.name,
                memberNumber = "${member.grade}${member.classNum}${String.format("%02d", member.number)}",
                memberRole = it.teamMemberRole,
                userId = member.id!!
            )
        }.toList()
    }

}