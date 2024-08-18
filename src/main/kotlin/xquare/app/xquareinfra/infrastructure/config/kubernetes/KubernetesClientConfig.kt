package xquare.app.xquareinfra.infrastructure.kubernetes.config

import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.apis.CustomObjectsApi
import io.kubernetes.client.util.ClientBuilder
import org.springframework.context.annotation.Bean
import javax.annotation.PostConstruct

@org.springframework.context.annotation.Configuration
class KubernetesClientConfig {
    @PostConstruct
    fun initKubernetesConfig() {
        val client = ClientBuilder.defaultClient()
        Configuration.setDefaultApiClient(client)
    }

    @Bean
    fun customObjectsApi() = CustomObjectsApi()

    @Bean
    fun coreV1API() = CoreV1Api()
}