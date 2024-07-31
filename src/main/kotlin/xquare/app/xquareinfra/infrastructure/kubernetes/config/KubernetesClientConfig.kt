package xquare.app.xquareinfra.infrastructure.kubernetes.config

import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.apis.CustomObjectsApi
import org.springframework.context.annotation.Bean
import javax.annotation.PostConstruct

@org.springframework.context.annotation.Configuration
class KubernetesClientConfig(
    private val kubernetesTokenRefreshScheduler: KubernetesTokenRefreshScheduler
) {
    @PostConstruct
    fun initKubernetesConfig() {
        kubernetesTokenRefreshScheduler.refreshKubernetesToken()
    }

    @Bean
    fun customObjectsApi() = CustomObjectsApi()

    @Bean
    fun coreV1API() = CoreV1Api()
}