package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.TeamMemberResponse
import xquare.app.xquareinfra.application.team.port.`in`.GetTeamDetailUseCase
import xquare.app.xquareinfra.application.team.port.out.ExistsUserTeamPort
import xquare.app.xquareinfra.application.team.port.out.FindUserTeamPort
import xquare.app.xquareinfra.infrastructure.persistence.team.UserTeamJpaEntity
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Transactional(readOnly = true)
@Service
class GetTeamDetailService(
    private val findTeamPort: xquare.app.xquareinfra.application.team.port.out.FindTeamPort,
    private val findUserPort: FindUserPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val findUserTeamPort: FindUserTeamPort
): GetTeamDetailUseCase {
    override fun getTeamDetail(teamId: UUID): DetailTeamResponse {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val user = readCurrentUserPort.readCurrentUser()

        if(!existsUserTeamPort.existsByTeamAndUser(team, user)) {
            throw XquareException.FORBIDDEN
        }

        val response = team.run {
            val admin = findUserPort.findById(adminId) ?: throw BusinessLogicException.USER_NOT_FOUND
            val userTeams = findUserTeamPort.findAllByTeam(team)
            DetailTeamResponse(
                teamNameEn = teamNameEn,
                teamNameKo = teamNameKo,
                memberCount = userTeams.size,
                adminName = admin.name,
                createdAt = createdAt!!,
                memberList = getMemberResponse(userTeams.toSet()),
                isAdmin = team.adminId == user.id!!
            )
        }
        return response
    }

    private fun getMemberResponse(members: Set<UserTeam>): List<TeamMemberResponse> {
        return members.map {
            val member = it.user
            TeamMemberResponse(
                memberName = member.name,
                memberNumber = "${member.grade}${member.classNum}${String.format("%02d", member.number)}",
                memberRole = it.role,
                userId = member.id!!
            )
        }.toList()
    }

}