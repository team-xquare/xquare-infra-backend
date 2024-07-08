package xquare.app.xquareinfra.domain.container.adapter.dto.request

data class SetContainerConfigRequest(
    val stag: ContainerConfigDetails?,
    val prod: ContainerConfigDetails?
)

data class ContainerConfigDetails(
    val branch: String,
    val containerPort: Int
)

