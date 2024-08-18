package xquare.app.xquareinfra.infrastructure.integration.vault

import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface VaultService {
    fun addSecret(secrets: Map<String, String>, path: String)
//    fun revoke(path: String)

    fun getPath(deploy: Deploy, container: Container): String
    fun getPath(deploy: Deploy): List<String>
}