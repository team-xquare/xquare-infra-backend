package xquare.app.xquareinfra.infrastructure.kubernetes.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("kubernetes")
@ConstructorBinding
data class KubernetesProperty(
    val kubeConfig: String
)