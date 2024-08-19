package xquare.app.xquareinfra.infrastructure.config.kubernetes.dto

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val token: String
)