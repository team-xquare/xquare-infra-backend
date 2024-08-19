package xquare.app.xquareinfra.adapter.`in`.container.dto.response

import xquare.app.xquareinfra.domain.container.model.ContainerStatus
import java.time.LocalDateTime

data class GetContainerDetailsResponse(
    val teamNameEn: String,
    val teamNameKo: String,
    val deployName: String,
    val repository: String,
    val domain: String,
    val lastDeploy: LocalDateTime,
    val containerStatus: ContainerStatus,
    val containerName: String,
    val isV2: Boolean
)
