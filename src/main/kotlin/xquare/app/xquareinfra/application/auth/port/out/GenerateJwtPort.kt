package xquare.app.xquareinfra.application.auth.port.out

interface GenerateJwtPort {

    fun generateTokens(subject: String): Pair<String, String>
}
