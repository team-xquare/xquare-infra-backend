package xquare.app.xquareinfra.infrastructure.integration.vault

import com.bettercloud.vault.Vault
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.deploy.domain.Deploy
import xquare.app.xquareinfra.infrastructure.exception.CriticalException

@Service
private class VaultServiceImpl(
    private val vault: Vault
): VaultService {

    companion object {
        const val XQUARE_PATH = "xquare-kv"
    }
    override fun addSecret(secrets: Map<String, String>, path: String) {
        val response = vault.logical().write("$XQUARE_PATH/$path", secrets)
        if(response.restResponse.status == 500) {
            throw CriticalException(500, "Vault Exception")
        }
    }

    override fun getPath(deploy: Deploy, containerJpaEntity: ContainerJpaEntity): String {
        if(deploy.isV2) {
            return "${deploy.deployName}-${containerJpaEntity.containerEnvironment.name}"
        }
        return "${deploy.deployName}-${deploy.deployType.name}-${containerJpaEntity.containerEnvironment.name}"
    }

    override fun getPath(deploy: Deploy): List<String> {
        return listOf(
            "${deploy.deployName}-prod",
            "${deploy.deployName}-stag"
        )
    }
}