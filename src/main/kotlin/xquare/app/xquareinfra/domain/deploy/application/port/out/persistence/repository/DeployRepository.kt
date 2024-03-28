package xquare.app.xquareinfra.domain.deploy.application.port.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import java.util.UUID

interface DeployRepository : JpaRepository<Deploy, UUID> {
    fun existsByDeployName(deployName: String): Boolean

    fun findByDeployName(deployName: String): Deploy?
}