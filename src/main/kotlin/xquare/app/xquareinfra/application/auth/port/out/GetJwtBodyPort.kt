package xquare.app.xquareinfra.application.auth.port.out

import io.jsonwebtoken.Claims
import java.security.PublicKey

interface GetJwtBodyPort {

    fun getJwtBody(token: String, publicKey: PublicKey): Claims
}
