package com.example.mergebackend.global.config.kubernetes.dto

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val token: String
)