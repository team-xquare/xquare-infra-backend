package xquare.app.xquareinfra.domain.container.adapter.dto.request

import xquare.app.xquareinfra.domain.container.domain.Language

data class SetContainerConfigRequest(
    val stag: ContainerConfigDetails?,
    val prod: ContainerConfigDetails?,
    val language: Language,
    val criticalService: Boolean = false
)

data class ContainerConfigDetails(
    val branch: String,
    val containerPort: Int,
    val domain: String
)

