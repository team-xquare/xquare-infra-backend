package xquare.app.xquareinfra.adapter.`in`.container.dto.request

import xquare.app.xquareinfra.domain.container.model.Language

data class CreateV3ApplicationRequest(
    val branch: String,
    val containerPort: Int,
    val domain: String,
    val language: Language,
    val criticalService: Boolean = false,
    val buildConfig: String,
    val buildDir: String
)
