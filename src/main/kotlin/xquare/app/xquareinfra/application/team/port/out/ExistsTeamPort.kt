package xquare.app.xquareinfra.application.team.port.out

interface ExistsTeamPort {
    fun existsByTeamNameEn(teamNameEn: String): Boolean
}