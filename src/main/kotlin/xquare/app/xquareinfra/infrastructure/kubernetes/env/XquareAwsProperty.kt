package com.example.mergebackend.global.env.kubernetes

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("xquare")
@ConstructorBinding
data class XquareAwsProperty(
    val accessKey: String,
    val secretKey: String
)