package xquare.app.xquareinfra.infrastructure.integration.vault

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface VaultService {
    fun addSecret(secrets: Map<String, String>, path: String)
//    fun revoke(path: String)

    fun getPath(deploy: Deploy, containerJpaEntity: ContainerJpaEntity): String
    fun getPath(deploy: Deploy): List<String>
}