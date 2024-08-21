package xquare.app.xquareinfra.infrastructure.persistence.deploy

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.deploy.model.DeployStatus
import xquare.app.xquareinfra.domain.deploy.model.DeployType
import java.util.*
import javax.persistence.*

@Entity(name = "tbl_deploy")
class DeployJpaEntity(
    id: UUID?,
    deployName: String,
    organization: String,
    repository: String,
    projectRootDir: String,
    oneLineDescription: String,
    teamId: UUID,
    secretKey: String?,
    deployStatus: DeployStatus,
    deployType: DeployType,
    useMysql: Boolean,
    v2: Boolean,
    useRedis: Boolean,
) : BaseUUIDEntity(id) {
    @Column(name = "deploy_name", nullable = false, unique = true)
    var deployName: String = deployName
        protected set

    @Column(name = "organization", nullable = false)
    var organization: String = organization
        protected set

    @Column(name = "repository", nullable = false)
    var repository: String = repository
        protected set

    @Column(name = "project_root_dir", nullable = false)
    var projectRootDir: String = projectRootDir
        protected set

    @Column(name = "one_line_description", nullable = false)
    var oneLineDescription: String = oneLineDescription
        protected set

    @Column(name = "team_id")
    var teamId: UUID = teamId
        protected set

    @Column(name = "secret_key", nullable = true)
    var secretKey: String? = secretKey
        protected set

    @Column(name = "deploy_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var deployStatus: DeployStatus = deployStatus
        protected set

    @Column(name = "deploy_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var deployType: DeployType = deployType
        protected set

    @Column(name = "use_redis", nullable = false)
    var useRedis: Boolean = useRedis
        protected set

    @Column(name = "is_v2", nullable = false)
    var v2 = v2
        protected set

    @Column(name = "use_mysql", nullable = false)
    var useMysql = useMysql
        protected set
}