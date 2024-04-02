package xquare.app.xquareinfra.domain.container.domain

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "tbl_container")
class Container(
    id: UUID?,
    deploy: Deploy,
    containerEnvironment: ContainerEnvironment,
    lastDeploy: LocalDateTime,
    subDomain: String?
) : BaseUUIDEntity(id) {
    @OneToOne
    @JoinColumn(name = "deploy_id")
    var deploy: Deploy = deploy
        protected set

    @Column(name = "container_environment", nullable = false)
    @Enumerated(EnumType.STRING)
    var containerEnvironment: ContainerEnvironment = containerEnvironment
        protected set

    @Column(name = "last_deploy", nullable = false)
    var lastDeploy: LocalDateTime = lastDeploy
        protected set

    @Column(name = "sub_domain", nullable = true)
    var subDomain: String? = subDomain
        protected set
}