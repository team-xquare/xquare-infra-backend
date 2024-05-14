package xquare.app.xquareinfra.domain.container.adapter.dto.response

import xquare.app.xquareinfra.domain.container.domain.ContainerStatus
import java.time.LocalDateTime

data class GetContainerDetailsResponse(
    val teamName: String,
    val deployName: String,
    val repository: String,
    val domain: String,
    val lastDeploy: LocalDateTime,
    val containerStatus: ContainerStatus
)
