package xquare.app.xquareinfra.application.auth.port.out

interface RevokeTokensPort {

    fun revoke(subject: String)
}
