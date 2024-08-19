package xquare.app.xquareinfra.application.team.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.application.auth.port.out.ReadCurrentUserPort
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponse
import xquare.app.xquareinfra.adapter.`in`.team.dto.response.SimpleTeamResponseList
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException

@Transactional
@Service
class GetMyTeamService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val findUserPort: FindUserPort,
    private val findDeployPort: FindDeployPort
): xquare.app.xquareinfra.application.team.port.`in`.GetMyTeamUseCase {
    override fun getMyTeam(): SimpleTeamResponseList {
        val user = readCurrentUserPort.readCurrentUser()

        val teamList = user.teams.takeUnless { it.isEmpty() }?.map { userTeam ->
            val deploys = findDeployPort.findAllByTeam(userTeam.teamJpaEntity)
            val admin = findUserPort.findById(userTeam.teamJpaEntity.adminId) ?: throw BusinessLogicException.USER_NOT_FOUND
            SimpleTeamResponse(
                teamId = userTeam.teamJpaEntity.id!!,
                teamType = userTeam.teamJpaEntity.teamType,
                teamNameKo = userTeam.teamJpaEntity.teamNameKo,
                teamNameEn = userTeam.teamJpaEntity.teamNameEn,
                administratorName = admin.name,
                deployList = deploys.map { it.deployName }

            )
        }?.sortedByDescending { it.teamNameEn } ?: arrayListOf()

        return SimpleTeamResponseList(teamList)
    }
}