package xquare.app.xquareinfra.application.auth.port.out

interface ReissuePort {

    fun reissue(refreshToken: String): Pair<String, String>
}
