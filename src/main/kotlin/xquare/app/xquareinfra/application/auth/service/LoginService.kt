package xquare.app.xquareinfra.application.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.adapter.`in`.auth.dto.request.LoginRequest
import xquare.app.xquareinfra.adapter.`in`.auth.dto.response.TokenResponse
import xquare.app.xquareinfra.application.auth.port.`in`.LoginUseCase
import xquare.app.xquareinfra.application.auth.port.out.GenerateJwtPort
import xquare.app.xquareinfra.application.user.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.adapter.out.external.dsm.client.DsmLoginClient
import xquare.app.xquareinfra.adapter.out.external.dsm.client.DsmLoginRequest

@Transactional
@Service
class LoginService(
    private val dsmLoginClient: DsmLoginClient,
    private val generateJwtPort: GenerateJwtPort,
    private val findUserPort: FindUserPort
): LoginUseCase {
    override fun login(loginRequest: LoginRequest): TokenResponse {
        val user = findUserPort.findByAccountId(loginRequest.accountId) ?: throw BusinessLogicException.USER_NOT_FOUND
        val userInfo = dsmLoginClient.getUserInfo(
            DsmLoginRequest(
                accountId = loginRequest.accountId,
                password = loginRequest.password
            )
        )

        val tokenPair = generateJwtPort.generateTokens(user.id.toString())
        return TokenResponse(
            accessToken = tokenPair.first,
            refreshToken = tokenPair.second,
            email = user.email
        )
    }
}
