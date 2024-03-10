package xquare.app.xquareinfra.domain.auth.application.port.out

interface ReissuePort {

    fun reissue(refreshToken: String): Pair<String, String>
}
