package xquare.app.xquareinfra.infrastructure.integration.vault

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface VaultService {
    fun addSecret(secrets: Map<String, String>, path: String)
//    fun revoke(path: String)

    fun getPath(deployJpaEntity: DeployJpaEntity, containerJpaEntity: ContainerJpaEntity): String
    fun getPath(deployJpaEntity: DeployJpaEntity): List<String>
}