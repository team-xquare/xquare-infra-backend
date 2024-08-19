package xquare.app.xquareinfra.infrastructure.integration.vault

import com.bettercloud.vault.Vault
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity
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

    override fun getPath(deployJpaEntity: DeployJpaEntity, containerJpaEntity: ContainerJpaEntity): String {
        if(deployJpaEntity.isV2) {
            return "${deployJpaEntity.deployName}-${containerJpaEntity.containerEnvironment.name}"
        }
        return "${deployJpaEntity.deployName}-${deployJpaEntity.deployType.name}-${containerJpaEntity.containerEnvironment.name}"
    }

    override fun getPath(deployJpaEntity: DeployJpaEntity): List<String> {
        return listOf(
            "${deployJpaEntity.deployName}-prod",
            "${deployJpaEntity.deployName}-stag"
        )
    }
}