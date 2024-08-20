package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.AddTeamMemberRequest
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.CreateTeamRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.request.DeleteTeamMemberRequest
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.DetailTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.TeamMemberResponse
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.team.port.`in`.TeamUseCase
import xquare.app.xquareinfra.application.team.port.out.*
import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.domain.team.model.Team
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import java.util.*

@Transactional
@Service
class TeamService(
    private val saveTeamPort: SaveTeamPort,
    private val existsTeamPort: ExistsTeamPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findUserPort: FindUserPort,
    private val existsUserTeamPort: ExistsUserTeamPort,
    private val findTeamPort: FindTeamPort,
    private val saveUserTeamPort: SaveUserTeamPort,
    private val findUserTeamPort: FindUserTeamPort,
    private val deleteUserTeamPort: DeleteUserTeamPort,
    private val findDeployPort: FindDeployPort
) : TeamUseCase {
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

        userTeams.add(UserTeam(
            id = null,
            user = currentUser,
            team = team,
            role = TeamMemberRole.ADMINISTRATOR
        ))

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

        saveTeamPort.saveTeamWithMembers(team, userTeams)
    }

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
            saveUserTeamPort.saveUserTeam(UserTeam(user = addMember, team = team, role = TeamMemberRole.MEMBER ))
        }
    }

    override fun deleteTeamMember(req: DeleteTeamMemberRequest, teamId: UUID) {
        val team = findTeamPort.findById(teamId) ?: throw BusinessLogicException.TEAM_NOT_FOUND
        val user = findUserPort.findById(req.userId) ?: throw BusinessLogicException.USER_NOT_FOUND

        val userTeam = findUserTeamPort.findByUserAndTeam(user, team) ?: throw BusinessLogicException.USER_TEAM_NOT_FOUND

        if(userTeam.role == TeamMemberRole.ADMINISTRATOR) {
            throw BusinessLogicException.USER_TEAM_BAD_REQUEST
        }
        deleteUserTeamPort.deleteByUserAndTeam(user, team)
    }

    override fun getMyTeam(): SimpleTeamResponseList {
        val user = readCurrentUserPort.readCurrentUser()

        val userTeams = findUserTeamPort.findAllByUser(user)
        val teamList = userTeams.map { userTeam ->
            val deploys = findDeployPort.findAllByTeam(userTeam.team)
            val admin = findUserPort.findById(userTeam.team.adminId) ?: throw BusinessLogicException.USER_NOT_FOUND
            SimpleTeamResponse(
                teamId = userTeam.team.id!!,
                teamType = userTeam.team.teamType,
                teamNameKo = userTeam.team.teamNameKo,
                teamNameEn = userTeam.team.teamNameEn,
                administratorName = admin.name,
                deployList = deploys.map { it.deployName }

            )
        }?.sortedByDescending { it.teamNameEn } ?: arrayListOf()

        return SimpleTeamResponseList(teamList)
    }

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