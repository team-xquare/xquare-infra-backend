package xquare.app.xquareinfra.domain.team.application.port.out

interface ExistsTeamPort {
    fun existsByTeamNameEn(teamNameEn: String): Boolean
}