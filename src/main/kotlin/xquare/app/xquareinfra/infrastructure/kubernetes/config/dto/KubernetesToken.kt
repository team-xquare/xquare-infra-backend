package com.example.mergebackend.global.config.kubernetes.dto

import kotlinx.serialization.Serializable

@Serializable
data class KubernetesToken(
    val status: Status
)