package xquare.app.xquareinfra.infrastructure.integration.vault

import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface VaultService {
    fun addSecret(secrets: Map<String, String>, path: String)
//    fun revoke(path: String)

    fun getPath(deploy: Deploy, container: Container): String
    fun getPath(deploy: Deploy): List<String>
}