package xquare.app.xquareinfra.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xquare.app.xquareinfra.domain.auth.adapter.dto.request.SignupRequest
import xquare.app.xquareinfra.domain.auth.application.port.`in`.SignupUseCase
import xquare.app.xquareinfra.domain.user.application.port.out.ExistsUserPort
import xquare.app.xquareinfra.domain.user.application.port.out.SaveUserPort
import xquare.app.xquareinfra.domain.user.domain.Role
import xquare.app.xquareinfra.domain.user.domain.User
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.DsmLoginClient

@Transactional
@Service
class SignupService(
    private val saveUserPort: SaveUserPort,
    private val dsmLoginClient: DsmLoginClient,
    private val existsUserPort: ExistsUserPort
): SignupUseCase {
    override fun signup(signupRequest: SignupRequest) {
        val userInfo = dsmLoginClient.getUserInfo(
            accountId = signupRequest.accountId,
            password = signupRequest.password
        )

        if(existsUserPort.existsByAccountId(signupRequest.accountId)) {
            throw BusinessLogicException.USER_ALREADY_EXISTS
        }

        userInfo.run {
            saveUserPort.saveUser(
                User(
                    id = null,
                    name = name,
                    accountId = accountId,
                    grade = grade,
                    classNum = classNum,
                    number = num,
                    roles = mutableListOf(Role.USER),
                    email = signupRequest.email
                )
            )
        }
    }
}