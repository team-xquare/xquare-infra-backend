package xquare.app.xquareinfra.domain.auth.service

import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.adapter.dto.request.LoginRequest
import xquare.app.xquareinfra.domain.auth.adapter.dto.response.TokenResponse
import xquare.app.xquareinfra.domain.auth.application.port.`in`.LoginUseCase
import xquare.app.xquareinfra.domain.auth.application.port.out.GenerateJwtPort
import xquare.app.xquareinfra.domain.user.application.port.out.FindUserPort
import xquare.app.xquareinfra.domain.user.application.port.out.SaveUserPort
import xquare.app.xquareinfra.domain.user.domain.Role
import xquare.app.xquareinfra.domain.user.domain.User
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.DsmLoginClient
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.dto.GetDsmUserInfoResponse

@Transactional
@Service
class LoginService(
    private val dsmLoginClient: DsmLoginClient,
    private val findUserPort: FindUserPort,
    private val generateJwtPort: GenerateJwtPort
): LoginUseCase {
    override fun login(loginRequest: LoginRequest): TokenResponse {
        val feignResponse = dsmLoginClient.getUserInfo(
            accountId = loginRequest.accountId,
            password = loginRequest.password
        )

        if(feignResponse.status() >= 400) {
            throw XquareException.UNAUTHORIZED
        }

        val userInfo = Json.decodeFromString<GetDsmUserInfoResponse>(feignResponse.body().toString())
        val tokenPair = generateJwtPort.generateTokens(userInfo.id)
        return TokenResponse(
            accessToken = tokenPair.first,
            refreshToken = tokenPair.second
        )
    }
}