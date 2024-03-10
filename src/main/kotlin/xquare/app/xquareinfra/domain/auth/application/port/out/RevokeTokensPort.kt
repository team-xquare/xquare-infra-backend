package xquare.app.xquareinfra.domain.auth.application.port.out

interface RevokeTokensPort {

    fun revoke(subject: String)
}
