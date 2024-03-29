package xquare.app.xquareinfra.domain.auth.service

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
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.DsmLoginClient

@Transactional
@Service
class LoginService(
    private val dsmLoginClient: DsmLoginClient,
    private val findUserPort: FindUserPort,
    private val saveUserPort: SaveUserPort,
    private val generateJwtPort: GenerateJwtPort
): LoginUseCase {
    override fun login(loginRequest: LoginRequest): TokenResponse {
        val userInfo = dsmLoginClient.getUserInfo(
            accountId = loginRequest.accountId,
            password = loginRequest.password
        )
        val user = findUserPort.findByAccountId(userInfo.accountId)
            ?: userInfo.run {
                saveUserPort.saveUser(
                    User(
                        id = null,
                        name = name,
                        accountId = accountId,
                        grade = grade,
                        classNum = classNum,
                        number = num,
                        roles = mutableListOf(Role.USER)
                    )
                )
            }
        val tokenPair = generateJwtPort.generateTokens(user.id.toString())
        return TokenResponse(
            accessToken = tokenPair.first,
            refreshToken = tokenPair.second
        )
    }
}