package xquare.app.xquareinfra.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.adapter.dto.request.LoginRequest
import xquare.app.xquareinfra.domain.auth.adapter.dto.response.TokenResponse
import xquare.app.xquareinfra.domain.auth.application.port.`in`.LoginUseCase
import xquare.app.xquareinfra.domain.auth.application.port.out.GenerateJwtPort
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.DsmLoginClient

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
            accountId = loginRequest.accountId,
            password = loginRequest.password
        )

        val tokenPair = generateJwtPort.generateTokens(user.id.toString())
        return TokenResponse(
            accessToken = tokenPair.first,
            refreshToken = tokenPair.second
        )
    }
}
