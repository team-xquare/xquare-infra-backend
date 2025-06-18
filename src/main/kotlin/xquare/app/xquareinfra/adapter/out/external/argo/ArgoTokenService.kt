package xquare.app.xquareinfra.adapter.out.external.argo

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.out.external.argo.client.ArgoAuthClient
import xquare.app.xquareinfra.adapter.out.external.argo.config.ArgoProperties
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ArgoAuthRequest

@Service
class ArgoTokenService(
    private val argoAuthClient: ArgoAuthClient,
    private val argoProperties: ArgoProperties
) {
    @Volatile
    private var token: String = argoProperties.auth.token

    @Synchronized
    fun refreshToken(): String {
        val response = argoAuthClient.login(
            ArgoAuthRequest(
                username = argoProperties.auth.username,
                password = argoProperties.auth.password
            )
        )
        if (response.statusCode.is2xxSuccessful) {
            token = response.body?.token ?: token
        }
        return token
    }

    fun getToken(): String {
        if (token.isBlank()) {
            refreshToken()
        }
        return token
    }
}
