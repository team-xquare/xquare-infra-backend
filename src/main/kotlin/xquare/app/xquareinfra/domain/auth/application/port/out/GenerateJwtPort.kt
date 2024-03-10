package xquare.app.xquareinfra.domain.auth.application.port.out

interface GenerateJwtPort {

    fun generateTokens(subject: String): Pair<String, String>
}
