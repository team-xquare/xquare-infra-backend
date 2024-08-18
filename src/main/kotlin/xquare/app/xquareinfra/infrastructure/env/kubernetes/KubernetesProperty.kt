package xquare.app.xquareinfra.infrastructure.env.kubernetes

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("kubernetes")
@ConstructorBinding
data class KubernetesProperty(
    val kubeConfig: String
)