package xquare.app.xquareinfra.infrastructure.kubernetes.config.dto

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val token: String
)