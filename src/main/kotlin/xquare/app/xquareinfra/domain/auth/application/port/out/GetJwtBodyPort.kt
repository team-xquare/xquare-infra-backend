package xquare.app.xquareinfra.domain.auth.application.port.out

import io.jsonwebtoken.Claims
import java.security.PublicKey

interface GetJwtBodyPort {

    fun getJwtBody(token: String, publicKey: PublicKey): Claims
}
