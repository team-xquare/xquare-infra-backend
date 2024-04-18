package com.example.mergebackend.global.env.kubernetes

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("kubernetes")
@ConstructorBinding
data class KubernetesProperty(
    val kubeConfig: String
)