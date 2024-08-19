package xquare.app.xquareinfra.infrastructure.config.kubernetes.dto

import kotlinx.serialization.Serializable

@Serializable
data class KubernetesToken(
    val status: Status
)