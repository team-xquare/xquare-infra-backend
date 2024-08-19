package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.TeamMemberResponse
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Transactional(readOnly = true)
@Service
class GetTeamDetailService(
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val findUserPort: FindUserPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
): xquare.app.xquareinfra.application.team.port.`in`.GetTeamDetailUseCase {
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
            val member = it.userJpaEntity
            TeamMemberResponse(
                memberName = member.name,
                memberNumber = "${member.grade}${member.classNum}${String.format("%02d", member.number)}",
                memberRole = it.teamMemberRole,
                userId = member.id!!
            )
        }.toList()
    }

}